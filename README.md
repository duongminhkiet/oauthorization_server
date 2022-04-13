# oauthorization_server

1. Java 17 
2. project: https://github.com/spring-projects/spring-authorization-server 
3. Add 3 projects into STS
<img src="/docs/0_add_projects2_sts.PNG" alt="RUN"/>
<br/>
4. Create database postresql 
<img src="/docs/2_db_after_run.PNG" alt="RUN"/>
<br/>
5. On desktop change host
<img src="/docs/1_config_host_local.PNG" alt="RUN"/>
<br/>
5. Run with order 1->2->3
6. open localhost:8080
username/pass: admin/admin
<img src="/docs/3_login_8080.PNG" alt="RUN"/>
<br/>
<img src="/docs/4_login_success.PNG" alt="RUN"/>
<br/>
<img src="/docs/5_code.PNG" alt="RUN"/>
<br/>
<img src="/docs/6_return.PNG" alt="RUN"/>
<br/>

SourceCode need note to get access_token from postman
<img src="/docs/7_login_token.PNG" alt="RUN"/>
<br/>

Data saved in database
<img src="/docs/8_login_token_db.PNG" alt="RUN"/>
<br/>

Get access_token from postman
<img src="/docs/9_token_api.PNG" alt="RUN"/>
<br/>

Get introspect from postman from above access_token
<img src="/docs/10_instropect.PNG" alt="RUN"/>
<br/>