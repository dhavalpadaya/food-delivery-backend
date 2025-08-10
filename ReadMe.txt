# Problem Statement

Imagine a delivery executive called Aman standing idle in Koramangala somewhere when suddenly his phone rings and notifies that he’s just been assigned a batch of 2 orders meant to be delivered in the shortest possible timeframe.

All the circles in the figure above represent geo-locations :
        C1 : Consumer 1

- C2 : Consumer 2
- R1 : Restaurant C1 has ordered from. Average time it takesto prepare a meal is pt1
- R2 : Restaurant C2 has ordered from. Average time it takes to prepare a meal is pt2

Since there are multiple ways to go about delivering these orders, your task is to help Aman figure out the best way to finish the batch in the shortest possible time.
For the sake of simplicity, you can assume that Aman, R1 and R2 were informed about these orders at the exact same time and all of them confirm on doing it immediately. Also, for travel time between any two geo-locations, you can use the haversine formula with an average speed of 20km/hr (basically ignore actual road distance or confirmation delays everywhere although the real world is hardly that simple ;) )

# FoodDeliverySystem

FoodDeliverySystem is a backend application designed to simulate and optimize food delivery operations. It models restaurants, customers, and orders, and calculates the most efficient delivery route based on real-world locations and preparation times. The system is built with clean architecture principles, making it easy to extend, test, and maintain.

# Tech Stack

- Java (modular architecture)
- SLF4J + Logback for logging
- JUnit for testing

# ApproachToCalculateDeliveryRoute

To calculate delivery routes, the system uses a strategy-based approach.

 The default strategy—`OptimalDeliveryRouteCalculatorStrategy`:
- Generates all valid task sequences (pickup before delivery)
- Evaluates each sequence based on travel time and prep time
- Selects the sequence with the minimum total delivery time

This design allows future strategies (e.g., FirstOrderFirstServer strategy etc.) to be plugged in without modifying core services.

# Other Features of this Application

- Customer Management: 
	Stores customer details and delivery locations.
- Restaurant Management: 
	Handles restaurant registration and location data.
- Order Lifecycle: 
	Supports order creation, completion, and removal with status tracking.
- Optimal Route Calculation: 
	Uses a strategy pattern to compute the most time-efficient delivery sequence.
- Location-Based Distance Calculation: 
	Implements haversine formula for real-world geo-distance.
- Singleton Services: 
	Ensures centralized state management for orders and restaurants.
- Robust Logging: 
	SLF4J-based logging for observability and debugging.
- Comprehensive Testing: 
	100% method and line coverage in the service layer.

# Configuration Provided
The system includes configurable constants to support extensibility:

- EARTH_RADIUS_KM: 
	Defined as 6,371.0 km for haversine-based distance calculations.
- Delivery Speed: 
	Passed as a parameter to route calculation, allowing dynamic tuning (e.g., 30 km/h, 50 km/h).
- Center Location Coordinates and allowed radius (in kilometers): 
	These define the geographic area within which the application supports delivery and pickup services.

# Assumptions Made
- All locations are within a supported delivery radius (e.g., Mumbai region).
- Delivery speed is constant and provided as input.
- All services are singletons and maintain in-memory state for simplicity.
- Meal preparation times are exact and start at time zero.
- Travel is along great‐circle paths; road networks, traffic, and other delays are ignored.
- Pickup and delivery handling times (beyond wait for prep) are negligible.
- All orders are small enough to carry simultaneously.
- The algorithm exhaustively searches all valid sequences (exponential in the number of orders); practical for small batches.