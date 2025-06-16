# Mineral Flow - Land
Welcome to the **Mineral Flow - Land** microservice, one of the three core components of the Mineral Flow solution. This service handles all land-side operations including:

- **Truck Arrival Management**:
    - License plate recognition and gate control
    - Appointment scheduling for incoming trucks
    - FIFO queue management for unscheduled arrivals

- **Weighbridge Operations**:
    - Assignment of weighbridge numbers
    - Generation of weighbridge tickets (WBT) with weight measurements
    - Warehouse number assignment for material dumping

- **Facility Monitoring**:
    - Real-time tracking of trucks on premises
    - Monitoring of arrival time compliance

## Set-up
### 1. Set Up the Database, Keycloak and RabbitMQ
1. **Database**:
    - Choose your preferred database (e.g., PostgreSQL, MySQL) and set it up.
    - Create a database for the application.
    - Note down the connection URL, username, and password for later configuration.

2. **Keycloak**:
    - Set up Keycloak.
    - Create a realm, client, and configure roles and users as per your requirements.

3. **RabbitMQ**:
   - Set up RabbitMQ
   - Set up RabbitMQ credentials (or use the default ones)
   - Update your application properties file with the connection details

## Running the Application
1. Ensure all containers are running. (Keycloak, database, RabbitMQ)
2. Ensure all projects are pulled and set up correctly (Land, Warehouse, Water)
3. Start the Spring Boot application.
4. Access the application at `http://localhost:8080` (backend API).


### Banner Configuration

This project uses a custom banner that appears in the console when the application starts. You can modify or disable the
banner by adjusting the `banner.txt` file or using the following settings in `application.properties`:

To disable the banner:

```properties
spring.main.banner-mode=off


