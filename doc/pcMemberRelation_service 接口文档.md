# pcMemberRelation_service接口文档

关联数据库 PCMemberRelationRepository

## service调用：user_service，meeting_service

## 从原始接口调整

### ——5./user/pcMemberMeeting	自己当pcm的会议 get new
```sql
http://user_service/user/findByUsername

http://meeting_service/meeting/findById
```

### ——8. /user/undealedNotifications  获取所有未被处理的邀请 get
```sql
http://user_service/user/findByUsername

http://meeting_service/meeting/findById
```

### ——9./user/invitationRepo	针对邀请回复 post
````sql
http://user_service/user/findByUsername

http://meeting_service/meeting/findByMeetingName
````

### ——10. /user/undealedNotificationsNum  获取未被处理的请求的数量 get
```sql
http://user_service/user/findByUsername

http://meeting_service/meeting/findById
```

### ——11. /user/alreadyDealedNotifications 获取已经处理的邀请 get
```sql
http://user_service/user/findByUsername

http://meeting_service/meeting/findById
```

### ——16./meeting/application		申请会议 post
```sql
http://user_service/user/findByUsername

http://meeting_service/meeting/findByMeetingName

http://meeting_service/meeting/save
```

###  ——18./meeting/pcmInvitation	pcm邀请 post
```sql
http://meeting_service/meeting/findByMeetingName

http://user_service/user/findByUsername
```


### ——19./meeting/invitationStatus	某一会议的邀请状态 get （chair查看）
```sql
http://meeting_service/meeting/findByMeetingName

http://user_service/user/findById
```

## 添加接口供其他服务调用

### /pcMemberRelation/findByMeetingIdAndStatus