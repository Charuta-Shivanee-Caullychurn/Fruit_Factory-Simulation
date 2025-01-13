# Fruit_Factory-Simulation
Using Concurrent Programming in java to make a fruit factory simulation

Asia Pacific Fruit Factory is an automated factory situated in Ipoh which produces canned
fruits. Concurrent Programming Concepts using Java Programming Language have been
used to simulate the factory operations. Multiple sections in the factory operate in parallel,
including the entry of cans, sterilisation, filling, sealing, labelling, and packaging stages.
Moreover, the factory manages downstream processes. Including loading boxes onto vans
for delivery. 

---ASSUMPTIONS---
General Assumption:
• 5% of cans have defects, are under filled or are mislabelled and move to the reject
grabber.
• Not all boxes will be sterilised, sealed and put in boxes because of the percentage
of defective cans and the statistics will not give whole numbers.
• The cans are filled one at a time.

Reject Grabber:
• Each section has its own reject grabber that detects and removes defective cans.
• I assumed that each reject grabber is operating independently but shares a central
statistics function to log defects.
• Each reject grabber runs in a separate thread and uses shared data structures for
centralised defect tracking.

Loading Area :
• Production resumes once the forklift trucks move boxes from the loading area.
• A semaphore will be used to manage loading area capacity, and threads representing
production will pause when space is unavailable.

Forklift Trucks:
• Each forklift truck will transport boxes from the loading area to one of the two
loading bays.
• Forklifts are prone to errors 2% of the time.
• If a forklift stalls, it will pause briefly and then resume.
• Synchronisation blocks will be used to access the loading area and loading bays
when there are random delays.
• Forklift has a 5% chance of breaking down

Delivery Vans and Loading Bays:
Vans can accommodate 18 boxes, after which they depart and a van only returns the
next day. This means only the remaining vans are available in the system.
• If both loading bays are occupied, vans must wait until a bay becomes available.
• The simulation will create a congested scenario where one van is waiting while the
other two bays are occupied.
• According to my program, I assumed that boxes go to loading bays in an alternate
order:
Box 1 going to loading bay 1
Box 2 going to loading bay 2
Box 3 going to loading bay 1
