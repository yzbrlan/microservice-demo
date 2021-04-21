# MeetingServiceController

关联数据库 MeetingRepository

## 从原有接口调整

### ——4./user/chairMeeting	自己当主席的会议 get new

### ——7. /user/availableMeeting	用户可以投稿的会议 get new

### ——39./meeting/finalPublish

### ——17./meeting/meetingInfo		获取特定某个会议的全部信息 get

### ——20./meeting/beginSubmission chair开启投稿 post

### ——32. /meeting/publish	review完成后发布review结果变更Meeting状态 post

### ——26./admin/queueingApplication 获取未审批会议(admin) get

### ——27. /admin/alreadyApplication 获取已经审批会议(admin) get

### ——28. /admin/ratify	审批会议 post

## 根据数据库方法对外提供接口，用于其他服务的调用

### /meeting/findById

### /meeting/findByMeetingName

### /meeting/findByStatus

### /meeting/findByStatusNot

### /meeting/findByChairName

### /meeting/findByStatusAndChairNameNot

### /meeting/save




