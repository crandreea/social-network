# social-network
A JavaFX-based social network application that enables users to create accounts, connect with friends, exchange messages, and receive notifications in real time.

## **Features**

**User Management:** User registration, authentication, and profile management  
**Friendship System:** Send, accept, and reject friend requests  
**Messaging:** Private conversations between connected users  
**Notifications:** Real-time system for friend requests  
**Community Analysis:** Identify social groups and community structures

## Technology Stack

* JavaFX for UI components
* PostgreSQL for data persistence
* JDBC for database connectivity
* Observer Pattern for reactive UI updates
* Factory and Singleton Patterns for resource management

## Setup and Installation
### Prerequisites

* Java JDK 11 or higher
* JavaFX SDK
* PostgreSQL database server
* Maven or Gradle (optional, for dependency management)

### Database Setup

1. Create a PostgreSQL database:

        sqlCopyCREATE DATABASE socialnetwork;

2. Run the database initialization script:

        CREATE TABLE utilizatori (
        uid BIGINT PRIMARY KEY,
        username VARCHAR(255) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL
        );

        CREATE TABLE prietenii (  
        uid1 BIGINT NOT NULL,  
        uid2 BIGINT NOT NULL,  
        date DATE NOT NULL,  
        status VARCHAR(50) NOT NULL,  
        PRIMARY KEY (uid1, uid2),  
        FOREIGN KEY (uid1) REFERENCES utilizatori(uid),  
        FOREIGN KEY (uid2) REFERENCES utilizatori(uid)  
        ); 

        CREATE TABLE messages (
        id BIGINT PRIMARY KEY,
        sid BIGINT NOT NULL,
        rid BIGINT NOT NULL,
        message TEXT NOT NULL,
        date TIMESTAMP NOT NULL,
        FOREIGN KEY (sid) REFERENCES utilizatori(uid),
        FOREIGN KEY (rid) REFERENCES utilizatori(uid)
        );

        CREATE TABLE notification (
        id BIGINT PRIMARY KEY,
        description TEXT NOT NULL,
        date TIMESTAMP NOT NULL,
        uid BIGINT NOT NULL,
        FOREIGN KEY (uid) REFERENCES utilizatori(uid)
        );   

        CREATE TABLE logged_users (
        username VARCHAR(255) PRIMARY KEY,
        login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );

### Environment Configuration

Create a .env file with database credentials:

        DB_URL=jdbc:postgresql://localhost:5432/...
        DB_USER=your_username  
        DB_PASSWORD=your_password 

### Building and Running
        # Compile the project
        javac -cp .:lib/* -d out src/**/*.java
        
        # Run the application
        java -cp out:lib/* ubb.scs.map.SocialNetworkApplication

## Future Enhancements
* Profile pictures and customization
* Group chats
* Friend recommendation engine