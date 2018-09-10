drop table inventory;
drop table users_inRoom;
drop table furniture_scale;
drop table items;
drop table playrecord;
drop table room;
drop table map;
drop table users;

CREATE TABLE USERS 
(
  ID VARCHAR2(30 BYTE) primary key 
, PW VARCHAR2(30 BYTE) 
, EMAIL VARCHAR2(50 BYTE) unique
, NAME VARCHAR2(30 BYTE) 
, NICKNAME VARCHAR2(30 BYTE) unique
, PROFILE VARCHAR2(20 BYTE)
);

CREATE TABLE MAP 
(
  NO number primary key
, user_id VARCHAR2(20 BYTE) 
, title VARCHAR2(20 BYTE) 
, content VARCHAR2(20 BYTE) 
, rank VARCHAR2(20 BYTE) 
, theme VARCHAR2(20 BYTE) 
, abgtime VARCHAR2(20 BYTE) 
, deadtime VARCHAR2(20 BYTE) 
, inputdate VARCHAR2(20 BYTE) DEFAULT sysdate 
, CONSTRAINT MAP_USEIID_FK FOREIGN KEY(USER_ID) references users(id)
);

drop sequence map_seq;
create sequence map_seq;



CREATE TABLE PLAYRECORD 
(
  NO NUMBER primary key
, USER_ID VARCHAR2(30) NOT NULL 
, CONTRIBUTION VARCHAR2(20) 
, MAP_NO number NOT NULL 
, INPUTDATE VARCHAR2(20) 
, PLAYTIME VARCHAR2(20) 
, RANK VARCHAR2(20) 
, CONSTRAINT PLAYRECORD_USEIID_FK FOREIGN KEY(USER_ID) references users(id)
, CONSTRAINT PLAYRECORD_mapno_FK FOREIGN KEY(map_no) references map(no)
);


CREATE TABLE ROOM 
(
  NO NUMBER primary key
, MAP_NO number 
, MASTER_ID VARCHAR2(30) 
, TITLE VARCHAR2(20) 
, ROOM_PW VARCHAR2(20) 
, status varchar(20)
, CONSTRAINT room_masterid_FK FOREIGN KEY(master_id) references users(id)
, CONSTRAINT room_mapno_FK FOREIGN KEY(map_no) references map(no)
);

drop sequence room_seq;
create sequence room_seq;

create table users_inRoom
(
  no number primary key,
  room_no number,
  user_id varchar2(30) unique,
  session_id varchar2(30),
  constraint UIR_roomno_FK foreign key(room_no) references room(no) on delete cascade,
  constraint UIR_userid_FK foreign key(user_id) references users(id) on delete cascade
);

drop sequence users_inRoom_seq;
create sequence users_inRoom_seq;

create table items
(
  no number primary key,
  item_name varchar2(20),
  type varchar2(20),
  theme varchar2(20),
  json_file varchar2(30),
  icon_file varchar2(30),
  maching_item number
);

drop sequence items_seq;
create sequence items_seq;

create table furniture_scale
(
  no number primary key,
  items_no number,
  scale number,
  constraint furniture_scale_no_FK foreign key(items_no) references items(no)
);

drop sequence furniture_scale_seq;
create sequence furniture_scale_seq;

create table inventory 
(
  no number primary key,
  item_no number,
  UIR_no number,
  constraint inventory_UIR_no_FK foreign key(UIR_no) references users_inRoom(no) on delete cascade,
  constraint inventory_UIR_itmeNo_FK foreign key(item_no) references items(no) on delete cascade
);

drop sequence inventory_seq;
create sequence inventory_seq;

insert into users values('aaa', 'aaa', 'aaa@gmail.com', 'aaa', 'aaa', 'profile_01');
insert into users values('bbb', 'bbb', 'bbb@gmail.com', 'bbb', 'bbb', 'profile_02');
insert into users values('ccc', 'ccc', 'ccc@gmail.com', 'ccc', 'ccc', 'profile_03');
insert into users values('ddd', 'ddd', 'ddd@gmail.com', 'ddd', 'ddd', 'profile_04');
insert into users values('eee', 'eee', 'eee@gmail.com', 'eee', 'eee', 'profile_05');

delete from inventory;
delete from users_inRoom;
delete from room;
delete from map;
delete from items;

insert into items values(items_seq.nextval, 'hammer1', 'tool', '', 'hammer1', 'hammer1.png', null);
insert into items values(items_seq.nextval, 'hammer2', 'tool', '', 'hammer2', 'hammer2.png', null);
insert into map values(map_seq.nextval, 'aaa', 'test', '', '', '', '', '', sysdate);
insert into room values(room_seq.nextval, 1, 'aaa', 'test', null, 'gaming');
insert into users_inRoom values(users_inRoom_seq.nextval, 1, 'aaa', '');
insert into users_inRoom values(users_inRoom_seq.nextval, 1, 'bbb', '');
insert into inventory values(inventory_seq.nextval, 1, 1);
insert into inventory values(inventory_seq.nextval, 2, 2);

insert into items values(items_seq.nextval, 'char1', 'funiture', 'Normal', 'chair01', 'chair.png', null);
insert into items values(items_seq.nextval, 'bed', 'funiture', 'Normal', 'bed', 'bed.png', null);
insert into items values(items_seq.nextval, 'old_bed', 'funiture', 'Normal', 'old_bed', 'old_bed.png', null);
insert into items values(items_seq.nextval, 'axe', 'tool', 'Normal', 'axe', 'axe.png', null);
insert into items values(items_seq.nextval, 'dollars', 'deco', 'Normal', 'dollars', '100-dollars.jpg', null);
insert into items values(items_seq.nextval, 'bookshelves', 'funiture', 'Normal', 'BSwap_FurniturePack', 'Furniture_Atlas_Diffuse_01.png', null);

commit;