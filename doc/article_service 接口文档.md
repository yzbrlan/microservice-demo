# article-service 接口文档

关联数据库 ArticleRepository

## service调用：user-service，meeting-service

## 从原有接口调整

### ——6. /user/authorMeeting	自己已经投稿的会议 get new
```sql
http://meeting-service/meeting/findByMeetingName
```

### ——12./user/articleDetail 	论文信息 get 

### ——13./user/articleSubmission 投稿 post
```sql
http://meeting-service/meeting/findByMeetingName

http://user-service/user/findByUsername
```

###  ——14./user/updateArticle	修改论文信息 post new
```sql
http://meeting-service/meeting/findByMeetingName

http://user-service/user/findByUsername
```

### ——31. /meeting/submissionList  author获取投稿的稿件信息 get new
```sql
http://meeting-service/meeting/findByMeetingName
```

## 对外提供增删改查操作
```sql
http://article/findById

http://article/findByMeetingNameAndStatus

http://article/save
```


