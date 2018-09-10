package sesoc.global.escape.webSocket;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import sesoc.global.escape.dao.RoomDAO;
import sesoc.global.escape.repository.RoomRepository;
import sesoc.global.escape.repository.UserRepository;
import sesoc.global.escape.vo.Room;
import sesoc.global.escape.vo.Users;
import sesoc.global.escape.vo.WaitingUsers;
import sesoc.global.escape.vo.WebsocketVO;

public class WebSocketHandler extends TextWebSocketHandler {

   public static List<WebsocketVO> sessionList = new ArrayList<>();
   private static Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
   private int roomNum;
   
   @Autowired
   private Provider<RoomRepository> provider_room;
   
   @Autowired
   private Provider<UserRepository> provider_user;
   
   /**
    * @param waitingUser
    * @return INSERT 성공여부
    * 대기방에 입장한 유저들의 정보를 DB에 추가
    */
   public int insertWaitingUser(WaitingUsers waitingUser){
      RoomRepository repo = provider_room.get();
      return repo.insertWaitingUser(waitingUser);
   }//getuserInfo
   
   /**
    * @param room
    * @return
    * 방장이 방을 나갔을 때 방에 대한 모든 DB가 삭제
    */
   public int deleteRoom(Room room){
      RoomRepository repo = provider_room.get();
      return repo.deleteRoom(room);
   }//deleteRoom
   
   /**
    * @param room
    * @return
    * 방이 대기중인지 게임중인지 판별
    */
   public Room roomInfo(WaitingUsers user){
      RoomRepository repo = provider_room.get();
      return repo.roomInfo(user.getRoom_no()+"");
   }//deleteRoom
   
   /**
    * @param waitinguser
    * @return DB에 있는 방장 정보 반환
    * DB에 있는 방장 정보만 불러온다
    */
   public WaitingUsers selectBySessionId(WaitingUsers waitinguser){
      RoomRepository repo = provider_room.get();
      return repo.selectBySessionId(waitinguser);
   }//selectBySessionId
   
   /**
    * @param waitinguser
    * @return DB에 있는 일반 유저에 대한 정보 반환
    * DB에 있는 일반 유저에 대한 정보만 불러온다
    */
   public WaitingUsers findUser(WaitingUsers waitinguser){
      UserRepository repo = provider_user.get();
      return repo.findUser(waitinguser);
   }//selectBySessionId
   
   /**
    * @param room
    * @return 해당 방에 대한 모든 유저들의 정보를 반환
    * 특정한 방에 접속에 있는 유저들의 대한 정보를 List로 반환한다
    */
   public List<WaitingUsers> selectAll(Room room){
      UserRepository repo = provider_user.get();
      return repo.selectWaitingUser(room);
   }//selectAll
   
   /**
    * @param sessionId
    * @return 
    * 방장이 아닌 일반 유저가 방에서 퇴장시에 DB에서 삭제
    */
   public int deleteNormalUser(String sessionId){
      UserRepository repo = provider_user.get();
      return repo.deleteNormalUser(sessionId);
   }//deleteNormalUser
   
   /**
    * @param sessionId
    * @return int
    * 방에 남아 있는 USER 수 반환
    */
   public int numberOfUsers(String sessionId){
      UserRepository repo = provider_user.get();
      return repo.numberOfUsers(sessionId);
   }//deleteNormalUser
   
   @Override
   public void afterConnectionEstablished(WebSocketSession session) throws Exception {
       System.out.println("afterConnectionEstablished");
   }//afterConnectionEstablished
    
   @Override
   public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
       //다른 사용자 로그인시 갱신에 필요한 메소드
       if (message.getPayload().toString().contains("|roomNum|") && message.getPayload().toString().contains("|userId|") && message.getPayload().toString().contains("|userPw|")) {
            String receivedMessage = message.getPayload().toString().replaceAll("|roomNum|", "");
            int aIndex = receivedMessage.indexOf("|roomNum|");
            roomNum = Integer.parseInt(receivedMessage.substring(0, aIndex));
            int bIndex = receivedMessage.indexOf("|userId|");
            String userId = receivedMessage.substring(aIndex, bIndex).replace("|roomNum|", "");
            
            // websocket 등록
            WaitingUsers testUser = new WaitingUsers(0, roomNum, userId, session.getId(), null, null);
            Thread.sleep(300);
            insertWaitingUser(testUser);
            sessionList.add(new WebsocketVO(roomNum, session, null, null));
            
            List<WaitingUsers> list = selectAll(new Room(roomNum, 0, null, null, null, 0, null));
            for (WaitingUsers waitingUsers : list) {
               for (WebsocketVO socket : sessionList) {
                  if(socket.getSession().getId().equals(waitingUsers.getSession_id())){
                     socket.getSession().sendMessage(new TextMessage("|Enter|"));
                  }//if
               }//inner for
            }//for
            
            return;
         } // if
       
       //게임 룸으로 이동시
       if(message.getPayload().toString().contains("|goToGameRoom|")){
          String roomNo = message.getPayload().toString().replace("|goToGameRoom|", "");
          //같은 방에 있는 USER 불러옴
          List<WaitingUsers> waitingUser_list = selectAll(new Room(Integer.parseInt(roomNo), 0, null, null, null, 0, null));
          
         for (WaitingUsers waitingUsers : waitingUser_list) {
            // 보낸 메세지를 통해서 같은 방으로 이동할 수 있게 HOW? roomNo 같이보내서 디비에 저장하면 돼
            for (WebsocketVO socket : sessionList) {
               if(socket.getSession().getId().equals(waitingUsers.getSession_id())){
                  if(!(socket.getSession().getId().equals(session.getId()))){
                     socket.getSession().sendMessage(new TextMessage("|goToGameRoom|"+roomNo));
                  }//inner if
               }//outer if
            }//inner for
         }//outer for
          
         // 이동할 user sessionList에서 제거
         for (WaitingUsers waitingUsers : waitingUser_list) {
            for(int i=0; i<sessionList.size(); i++){
               WebsocketVO socket = sessionList.get(i);
               if(waitingUsers.getSession_id().equals(socket.getSession().getId())){
                  sessionList.remove(socket);
               }//if
            }//inner for
         }//outer for
          return;
       }//if
       
         //메세지 전송 준비
         for (WebsocketVO data : sessionList) {
            String msg = message.getPayload().toString();
            int index = msg.indexOf("*");
            String roomNo = message.getPayload().toString().substring(0, index);
            
            String nickname = "";
            WaitingUsers user = null;
            for (WebsocketVO webVO : sessionList) {
               if(webVO.getSession().getId().equals(session.getId())){
                  user = findUser(new WaitingUsers(0, 0, null, webVO.getSession().getId(), null, null));
                  nickname = user.getNickname();
               }//if
            }//inner for
            
            //메세지 전송
            List<WaitingUsers> userList = selectAll(new Room(Integer.parseInt(roomNo), 0, null, null, null, 0, null));
            for (WaitingUsers waitingUsers : userList) {
               if(waitingUsers.getSession_id().equals(data.getSession().getId())){
                  data.getSession().sendMessage(new TextMessage(nickname + " : " + msg.substring(index+1)));
               }//if
            }//outer fore
            
            
         } // for
       
   }//handleMessage
    
    @Override
   public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
       System.out.println("afterConnectionClosed");
       whenExit(session);
   }//afterConnectionClosed
    
    /**
     * @param session
     * @throws Exception
     * 유저가 방에서 퇴장했을 때에 대한 로직처리
     */
    public void whenExit(WebSocketSession session) throws Exception{
       
       WaitingUsers user = selectBySessionId(new WaitingUsers(0, 0, null, session.getId(), null, null));
       
       if(user != null){
          Room room = roomInfo(user);
          if(room.getStatus().equals("waiting")){
            whenMaster_Exit(session, user);
          }else{
             whenNormalUser_Exit(session, new Room(user.getRoom_no(), 0, null, null, null, 0, null)); // 게임방 일 때
          }//inner else-if
       }else{
          whenNormalUser_Exit(session, null);
       }//else-if
       
    }//whenExit
    
    /**
     * @param session
     * @throws Exception
     * 일반 유저 퇴장시 삭제
     */
    public void whenNormalUser_Exit(WebSocketSession session, Room room) throws Exception{

       if(numberOfUsers(session.getId()) == 1){
          deleteRoom(new Room(roomNum, 0, null, null, null, 0, null));
       }//if
       WaitingUsers user = findUser(new WaitingUsers(0, 0, null, session.getId(), null, null));
       deleteNormalUser(session.getId());
       
       for (WebsocketVO websocketVO : sessionList) {
          if(websocketVO.getSession().getId().equals(session.getId())){
             sessionList.remove(websocketVO);
             break;
          }//if
       }//for
       
       System.out.println("whenNormalUser_Exit : " + room);
       for (WebsocketVO data : sessionList) {
          if(user != null){
             data.getSession().sendMessage(new TextMessage(user.getNickname() + " 님이 퇴장하셨습니다."));
          }//if
          data.getSession().sendMessage(new TextMessage("|Enter|"));
       }// for
    }//deleteNormalUser
    
    /**
     * @param session
     * @param user
     * 방장 퇴장 시 삭제
     */
    public void whenMaster_Exit(WebSocketSession session, WaitingUsers user) throws Exception{
       List<WaitingUsers> deleting_list = selectAll(new Room(user.getRoom_no(), 0, null, null, null, 0, null));
       deleteRoom(new Room(user.getRoom_no(), 0, null, null, null, 0, null));
       for (WaitingUsers waitingUsers : deleting_list) {
          for (WebsocketVO websocketVO : sessionList) {
             
             if(websocketVO.getSession().getId().equals(waitingUsers.getSession_id())){
                if(!(websocketVO.getSession().getId().equals(session.getId()))){
                   websocketVO.getSession().sendMessage(new TextMessage("|room_deleted|"));
                }//inner if
                sessionList.remove(websocketVO);
                break;
             }//if-else
          }//inner for
       }//outer for
    }//whenMaster_Exit
}//class