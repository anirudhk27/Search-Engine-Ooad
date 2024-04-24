# Search Engine

This project concerns the implementation of a simple search engine.
The Applications is devided into two parts. Front-End and Back-End. Below you can see in more detail the structure of the software.

**FRONT END**

Fornt-end is actually a simple interface where the user can access the search engine.
```
  index.html
  script.js
  style.css
```


**BACK END**

All the work for crawling, indexing and pricessing queries is done here.

- application
  ```
  Application.java
  Response.java
  RouteController.java
  ```
- services
  - crawler
    ```
    CrawlTask.java
    Crawler.java
    ```
  - indexer
    ```
    Indexer.java
    IndexingTask.java
    ```
  - query_processor
    ```
    Query.java
    ```
- util
  ```
  ArrayIndexerComparator.java
  HtmlDocument.java
  StopWords.java
  Tupple.java
  ```
 
 ## How to run?
 
 ### Requirements
 
 * Chrome extension : [Allow CORS: Access-Control-Allow-Origin](https://chrome.google.com/webstore/detail/allow-cors-access-control/lhobafahddgcelffkeicbaginigeejlf?hl=en)
 * [Java 13.0.2](https://www.oracle.com/technetwork/java/javase/downloads/jdk13-downloads-5672538.html) or higher
 * [intellij idea](https://www.jetbrains.com/idea/) (if you choose the second option)
 * [maven](https://maven.apache.org/) (if you choose the second option)
 
 ### Options to run the project 
 
 **First Way : download the precompiled .jar file from the repository**
 
  1. Download search-engine-1.0-SNAPSHOT.jar
  2. Open the command-line at the folder you have downloaded the .jar file
  3. Use the following command ```java -jar search-engine-1.0-SNAPSHOT.jar <website> <number of pages to crawl> <number of threads> <use old data?(true/false)> <no crawling?(true/false)> ```
  4. Wait the server to initialize (about 1-2 minutes - depends on the hardware used)
  5. Open the ```index.html``` file and try to search something by giving a query and the number of pages you want to get as a result.
  
 **Second Way : download the whole project and using maven generate your own .jar file**
 
  1. Download the project from the repository 
  2. Import the project in you IDE - intellij Idea
  3. Perform maven Lifecycle  ```clean``` operation
  4. Perform maven Lifecycle  ```package``` operation
  5. The .jar file is produced and saved in the created folder with name ```Target```
  6. The following steps are the same as in the previus option
  
## Tutorial

In the following tutorial we will be testing the software using a precalculated dictionary which was created from the following website : (https://en.wikipedia.org/wiki/Cabinet_of_Kyriakos_Mitsotakis)

This website has information about the current Greek coverment formation - Ministers, Ministries and the Prime Minister.

First of all copy and paste in your .jar file directory the following files:

[Dictionary](https://github.com/Erodotos/SearchEngine/blob/master/dictionary.txt)

[Sources](https://github.com/Erodotos/SearchEngine/blob/master/sources.txt)

[Stopwords](https://github.com/Erodotos/SearchEngine/blob/master/stopWords.txt)


The next step is to run the following command in the directory where we have placed the above files, which is the same directory as search-engine-1.0-SNAPSHOT.jar

```java -jar search-engine-1.0-SNAPSHOT.jar https://en.wikipedia.org/wiki/Cabinet_of_Kyriakos_Mitsotakis 200 8 true true```

### Click on the image below to view demonstration video

[![Demo Video](https://i.imgur.com/KMkNXMw.png)](https://www.youtube.com/watch?v=48hRFiGA6_w)


