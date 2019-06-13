


dockerized vernemq with graphite and grafana 
=======


----------


what is?
-------
[Graphite][1] is a django application that receives and displays metrics data from various sources. [Docker][2] is a technology that allows you to package an application with all of its dependencies into a standardized unit (aka a container) for software development. What's here are the docker build files necessary to stand up and containerize a graphite server. Grafana is a front end to make graphite prettier. Vernemq is an MQTT broker. 


what you need before you get started
------------------------------------
First you're going to need Docker: 

 - [for Linux][3]
 - [for OSX][4]
 - [for Windows][5]

You're also going to need [Docker Compose][6]. This will automate the process of container provisioning + runtime management.  


how to make it go
-----------------

**1)** From the **../dockerStuff** directory, type 

> **docker-compose -f compose/docker-compose.yml up -d**

*NOTE: the -f flag allows you to run the docker-compose command from anywhere by specifying the location of the docker-compose.yml file.* 

**2)** If and only if this is the first time you're bringing up the server, you need to sync the database and create a root user. This will allow you to customize your graphite installation, save graphs, etc. 

log into the graphite container thusly:
>**sudo docker exec -t -i graphite /bin/bash**

then enter:
>**graphite-manage syncdb**

you should see something like this:

    root@a33fc449be3d:/etc/graphite# graphite-manage syncdb  
    Creating tables ...  
    Creating table account_profile  
    Creating table account_variable  
    Creating table account_view  
    Creating table account_window  
    Creating table account_mygraph  
    Creating table dashboard_dashboard_owners  
    Creating table dashboard_dashboard  
    Creating table events_event  
    Creating table auth_permission  
    Creating table auth_group_permissions  
    Creating table auth_group  
    Creating table auth_user_groups  
    Creating table auth_user_user_permissions  
    Creating table auth_user  
    Creating table django_session  
    Creating table django_admin_log  
    Creating table django_content_type  
    Creating table tagging_tag  
    Creating table tagging_taggeditem 
    
    You just installed Django's auth system, which means you don't have any superusers defined.
    Would you like to create one now? (yes/no): 

enter 

> **yes**

and respond to the prompts to create an authorized user. Don't forget to write down the username/password for the account you create - you'll need it later! 

Now restart the carbon-cache service:

> **sudo service carbon-cache restart**

then exit out of the container by typing

> **exit**

**3)** Open your browser and goto localhost. You should see the application. If you just synced the database and see a page indicating an internal server error, refresh the page. What you're looking for is evidence carbon has registered itself. If you don't see carbon listed under the Graphite folder on the left-hand side, wait 60seconds and refresh the page. Carbon, by default, will report to Graphite every 60s so if it hasn't shown up yet you probably just need to wait for the next report cycle to occur. 

**4)** now we need to update a few configs in the vernemq container. login as root thusly:

`docker exec -u 0 -it vernemq1 bash`

then execute command:

`vmq-admin cluster show`

you should see a response that looks something like: 

```sh
root@785a8ea6f73e:/vernemq# vmq-admin cluster show
+------------------+-------+
|       Node       |Running|
+------------------+-------+
|VerneMQ@{SOME_IP@}| true  |
+------------------+-------+
```

w00t. now we need to enable metrics reporting to grpahite. take the node name listed in the table and run the following  command:
`vmq-admin set graphite_host=$GRAPHITE_PORT_80_TCP_ADDR graphite_enabled=on -n VerneMQ@{IP_ADDRESS_FROM_PREVIOUS_STEP_HERE}`

how to make it stop
-------------------
From the **../dockerStuff** directory, type 

> **docker-compose -f compose/docker-compose.yml stop**




pimp your graphite ride
-----------------------
There's lots of neato-burrito stuff you can do to graphite to make it look cooler and do more stuff. You can log into your graphite server via the web interface by clinking the 'login' link in the upper right corner of the screen. [Here][7] are the graphite docs on working with dashboards and [here][8] is a list of ready-bake dashboards you can install. (in this setup grafana has been provided for you) 

troubleshooting 
-----------------------

 - No metrics data? Try checking to see if the carbon service is running and/or needs to be restarted inside the graphite container. After you've started the containers, type this:

> **docker exec -t -i graphite bash**

 which should log you into the container via bash. Once you're there type:

> **sudo service --status-all**

if you don't see  *[ + ]  carbon-cache* listed in the results, type:

> **sudo service carbon-cache start**

else, try restarting the service

> **sudo service carbon-cache restart**

then

> **exit**

to log out of the container. Refresh the graphite webpage and try again. You should see metrics data at this point. 

 - to log into a container (the vernemq one in particular) as root: 
 `docker exec -u 0 -it vernemq1 bash`

  [1]: http://graphite.readthedocs.org/en/latest/
  [2]: http://www.docker.com/what-docker
  [3]: https://docs.docker.com/linux/started/
  [4]: https://docs.docker.com/mac/started/
  [5]: https://docs.docker.com/windows/started/
  [6]: https://docs.docker.com/compose/install/
  [7]: http://graphite.readthedocs.org/en/latest/dashboard.html
  [8]: http://dashboarddude.com/blog/2013/01/23/dashboards-for-graphite/


