# CAMEL-HARNESS


### what is this? 

This is the reference implementation for Harnessed [Apache Camel](http://camel.apache.org/) microservices. Harness is a featherweight microservices framework that allows you to "Harness" your Camel ride in various was to make building/testing/managing Camel services easier (see link to the Harness repository for more details on what Harness can do for you). The source code for the core Harness library can be found [here](https://github.com/davidholiday/camel-harness-lib).  __Use this template as the starting point when greenfielding a new Camel microservice.__


### how to get started

* Make sure you have [java8](https://openjdk.java.net/) and [apache maven](http://maven.apache.org/) 

* Once you clone the repo, you're going to need to make a few changes to the pom file. 
    * Change the ```artifactId``` field to match the name of the new service.
        
    * Change the ```warName``` field to match the name of the new service.
    
    * Ensure the ```source``` and ```target``` versions identified in the ```maven-compiler-plugin``` matches the version of jdk you're using.
    
* Give it a test drive
    * At the terminal, from the project root directory, execute command ```mvn clean install```. This will compile and run tests.
    
    * Then execute command ```mvn:tomcat7 run``` to fire up the service.
    
    * In your browser, goto URL ```localhost:8889/{{WARNAME}}/healthcheck```where {{WARNAME}} is whatever you put into the ```warName``` field in the pom file. If everything is working correctly you should receive an empty JSON response and a HTTP response code of 200. 


### what's here? 

* __./docker/test__ is where the docker file and scripts that facilitate local testing live. For example, if you are writing a camel service that works with a kafka queue, you would use the contents of this directory to spin up whatever external dependencies your service requires to work. An example of how to fire up multiple services in a controlled way (Zookeeper + Kafka) is provided. 

* __com.davidholiday.camel.harness...__
    * __beans__ is where your java [beans](https://stackoverflow.com/questions/3295496/what-is-a-javabean-exactly#3295517) will go.
    
    * __config__ is where you will load config properties. It uses a helper from Harness that allows us to easily look for properties in multiple locations. The default behaviour of [Archaius](https://github.com/Netflix/archaius) is to look for properties in ```./src/main/resources/config.properties```. By convention, properties defined in the local ```config.properties``` file should end with a *.local tag to differentiate them from analogue properties loaded from either the config-sidecar or another source. The loader will try to get properties from the first source and, failing that, will look to the second source. This way the service will always automagickally load with the appropriate set of properties. 
    
    * __context__ is where the [CamelContextLifecycle](http://www.javadoc.io/doc/org.apache.camel/camel-servletlistener/2.23.1) objects live (we refer to them as *AppContextLifecycle* objects to encourage developers not familiar with Camel to remember the Camel context is in effect the context object for the entire app). This is where you'll register objects with [JNDI](https://en.wikipedia.org/wiki/Java_Naming_and_Directory_Interface), establish listeners for app events, and in general establish app-wide behaviors that are triggered at the various lifecycle stages of your app. 
        * In this package you'll see a *base* version of the lifecycle object as well as a *harnessed* one. Use the *base* version to establish default behaviors for your app and the *harnessed* one to enable context-specific overrides as well as testing. 
        * You define the lifecycle object you want your app to use in ```./src/main/webapp/WEB-INF/web.xml```. 
        
    * __processors__ is where your camel [processors](http://camel.apache.org/processor.html) will live.
    
    * __routebuilders__ is where your camel [routebuilders](http://camel.apache.org/routebuilder.html) are stored. It is strongly encouraged developers harness their routebuilders in order to facilitate testing and dynamic behavior.
    
    * __util__ is fairly self-explanatory. Provided is ```ConnectionStringFactory``` which is a single-source-of-truth for any connection strings used by RouteBuilders to resolve where traffic should be routed. 
    
* __com.davidholiday.camel.harness.test...__
    * __beans__ where bean tests can be found.
    
    * __PrePostTestProcessors__ harnessed processors and routebuilders may plug into test harnesses that provide pre-made test routes that have nodes for pre and post test processors. The processors used to plug into those nodes are located here. 
    
    * __processors__ where the processor tests can be found.
    
    * __routebuilders__ where the routebuilder tests can be found. 
  
* __./src/main/resources/__ is where the default ```config.properties``` and mock files for the app live. 


### how do I...

* __start the service__

    * When running locally you can use the command ```mvn tomcat7:run``` to spin things up. In prod the war file generated by the build process will itself be plopped into a tomcat container. 

* __override app behavior based on runtime context?__

    * this is what the AppContextLifecycle Harness is for. In a nutshell, you'll write a method that overrides one of the default AppContextLifecycle stage methods. See ```com.davidholiday.camel.harness.context.HarnessedAppContextLifecycle``` for example. 

* __have the service use a mock in some runtime contexts but not in others?__

    * Let's say one of your routes will make a RESTful call to another service when deployed but for testing and local runtime context you want it to look to a mock. The way you accomplish this is to set the mockfile name in the ```config.properties``` file as well as a service source property that points to a registered processor who loads and returns the contents of said mockfile. Then, in your ```Properties``` class you'll create a property reference that first attempts to load said property using the 'deployed' key then, failing that, from the 'local' key. If the app is deployed and the 'deployed' property key is defined the app will make RESTful calls. If that key isn't there then the app will default to using the mockfile and associated processor. The template demonstrates this - see the aforementioned classes for an example. 

* __manage the lifecycle of the app during tests?__

    * Unfortunately you can't because of the way the underlying CamelTestSupport object works with respect to managing the context lifecycle. Because we use the AppContextLifecycle object to manage JNDI we use it also in test to manage the contents of the JNDI registry. 

* __know what the url of the service will be when I start it?__

    * while the hostname and port properties are environment specific, when in local mode the default hostname is ```localhost``` and the default port is ```8889```. the tomcat app context name is set to whatever the ```warName``` field is defined as in the pom.xml file. See "how to get started" in this document for an example. 
    
* __reboot routebuilders based on configuration property changes?__ 

    * see the *afterStart()* method in class ```AppContextLifecycle```. It hooks into a class provided by Harness that allows you to listen for particular property changed events and respond by firing off a runnable that reboots Routebuilder contents. 
