# rebuttal_service 接口文档

关联数据库 RebuttalRepository

## service调用：meeting_service article_service

## 从原有接口调整

### ——37./meeting/rebuttal
```sql
http://meeting_service/meeting/findByMeetingName

http://article_service/article/findById

http://meeting_service/meeting/save
```

### ——38./meeting/rebuttalInfo

