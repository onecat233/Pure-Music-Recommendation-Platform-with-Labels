# Pure Music Recommendation Platform with Labels

## Project Overview
Pure Music Recommendation Platform is a comprehensive music recommendation system that provides both user and admin interfaces for managing and exploring music with intelligent labeling features.

## Prerequisites

### System Requirements
- Vue.js 3.0+
- Vue Router
- Node.js v18.16.0 or higher
- Java 1.8
- MySQL
- Recommended IDEs: VSCode or IntelliJ IDEA

### Configuration Notes
- Ensure MySQL connector configuration is correctly set up
- Default database credentials can be modified in the source code

## Installation Steps

### 1. Project Setup
- Extract project files to `D:\learning_materials\master\project`
- If using a different directory, update file paths accordingly

### 2. Database Configuration
#### Create MySQL Database
1. Create a new database named `music`
2. Default credentials:
   - Username: `root`
   - Password: `cwq030120`
- Replace these values as per your MySQL settings.
   
#### Create Required Tables
Execute the following SQL commands:

```sql
CREATE TABLE admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name TEXT NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE `user` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE song (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name TEXT NOT NULL,
    url TEXT NOT NULL
);

CREATE TABLE tag (
    id INT AUTO_INCREMENT PRIMARY KEY,
    description TEXT NOT NULL
);

CREATE TABLE favour (
    userId INT NOT NULL,
    songId INT NOT NULL,
    PRIMARY KEY (userId, songId),
    FOREIGN KEY (userId) REFERENCES `user`(id) ON DELETE CASCADE,
    FOREIGN KEY (songId) REFERENCES song(id) ON DELETE CASCADE
);

CREATE TABLE type (
    songId INT NOT NULL,
    tagId INT NOT NULL,
    agreeNum INT DEFAULT 0,
    PRIMARY KEY (songId, tagId),
    FOREIGN KEY (songId) REFERENCES song(id) ON DELETE CASCADE,
    FOREIGN KEY (tagId) REFERENCES tag(id) ON DELETE CASCADE
);
```

### 3. Backend Setup
Navigate to the project directory and start the backend:
```bash
cd music
mvn spring-boot:run
```

### 4. Frontend Admin Interface
```bash
cd frontend-admin
npm install
npm run serve
```
- Open the generated link in Google Chrome
- Access admin perspective and perform operations

### 5. Frontend User Interface
```bash
cd ../frontend-user
npm install
npm run serve
```
- Open the generated link in Google Chrome
- Access user perspective and explore features

## Maintenance and Contact

### Project Status
- Java Final Project
- Waiting to be scored
