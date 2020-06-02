# Multiuser_Chat_Application
Server-Client based Multiuser Chat Application using Socket Programming in JAVA
IDE Used: 		Eclipse IDE for Java Developers
Version: 2019-12 (4.14.0)
Build id: 20191212-1212

Plugin for GUI: 	Window Builder in Eclipse

Run Instructions:
To run Source Files:
1.	Launch Eclipse IDE
2.	Import Message_Server.zip file from import options under File tab.
3.	Run only one ServerGUI.java to launch ServerGUI and Follow “To use GUIs:” instructions for serverGUI
4.	Run one or more ClientGUI.java to instantiate clients and Follow “To use GUIs:” instructions for clientGUI

Error conditions for ServerGUI:
1.	After you start server by clicking Start Server button, you can still click the start server button but it would lead to error as port number for Server is hard coded. And clicking Start Server button would hit the same port. (portNumber=9999 & ip = localhost or 127.0.0.1)
2.	You can close the server by clicking Stop Server button but you can’t start server from same instance. You will have to close ServerGUI and Start it again.
3.	Directly closing ServerGUI will lead to errors at ClientGUI

Error conditions for ClientGUI:
1.	After you start client by clicking Start Chat button, after this you can still click the Start Chat button but it would take you to new thread and you will have to start from login again.
2.	Any message without mentioning the recipient will be send to server and displayed at server output screen. 
3.	Even if you close ClientGUI and you have entered some username ABC to login, you can’t use the same username to login until you have restarted the server.
4.	Directly closing ClientGUI will lead to errors at ServerGUI

