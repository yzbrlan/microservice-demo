# ReviewRelationController接口文档

跟数据库reviewRepository关联
## service调用：user_service，meeting_service, article_service

## 从原有接口调整

### 15. /user/reviews	投稿者获取所有审稿意见 get new

`http://article_service/article/findById`

### 35./meeting/updateReview

`http://meeting_service/meeting/findById`

`http://article_service/article/findById`

`http://article_service/article/save`

`http://user_service/user/findByUsername`

### 36./meeting/reviewConfirm

`http://meeting_service/meeting/findById`

`http://user_service/user/findByUsername`

`http://meeting_service/meeting/save`

### 21./meeting/reviewArticles	获取需要审的稿件信息 get new

`http://article_service/article/findById`

`http://user_service/user/findByUsername`

`http://meeting_service/meeting/findByMeetingName`

### 22./meeting/reviewArticle 	获取某一篇需要审的稿件具体信息 get new

`http://article_service/article/findById`

`http://meeting_service/meeting/findByMeetingName`

`http://user_service/user/findByUsername`


### 23./meeting/reviewer	作为审稿人审稿 post new

`http://meeting_service/meeting/findByMeetingName`

`http://article_service/article/findById`

`http://meeting_service/meeting/save`

### 24. /meeting/alreadyReviewedInfo	审稿人获取某一篇已经审稿的信息 get new

`http://user_service/user/findByUsername`

### 25. /meeting/beginReview 开启审稿 post new

`http://meeting_service/meeting/findByMeetingName`

`http://article_service/article/findByMeetingNameAndStatus`

`http://meeting_service/meeting/save`

`http://user_service/user/findByFullnameAndEmail`

`http://pcMemberRelation_service/pcMemberRelation/findByMeetingIdAndStatus`

