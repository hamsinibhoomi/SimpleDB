Unity Ids
----------
Sundeep Kota - skota3
Hamsini Bhoomi - hbhoomi
Amulya Perugupallu - aperugu

Code Edits:
-----------
The following Java programs have been changed 

1) package: simpledb.file, file: Page.java

created getter and setter api for the data types short, byte[], boolean, date as given in the task 3 description of the project.

2) package: simpledb.buffer, file: BufferMgr.java, BasicBufferMgr.java

BufferMgr.java - added an extra constructor to take in the lru_k (int) parameter
added a overloaded method for pin(block, time) which takes in time as a parameter

BasicBufferMgr.java - removed the bufferPool array and replaced it with an HashMap
edited almost all the methods to take in the above change. 
Also implemented LRU_K algorithm for choosing a buffer.

3) package: simpledb.server

SimpleDB.java -  added a static variable available throughout the project.

Test Cases
-----------

All the test cases for the three tasks are placed in the package simpledb.test