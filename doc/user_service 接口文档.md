# user_service 接口文档

关联数据库 UserRepository

## 从原有接口调整

### /welcome 欢迎 get

### ——1./register 注册 post

### ——2./login 登录 post

### ——3./user/userinfo 用户信息 get

### ——29. /util/users		通过fullname查询user get

### ——30. /utils/pdf 获取投稿pdf内容信息 get

## 根据数据库方法对外提供接口，用于其他服务的调用

### /user/findByUsername

###  /user/findById

### /user/findByEmail

### /user/findByFullnameContains

### /user/findByFullnameAndEmail

