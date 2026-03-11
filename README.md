# Pandemic Simulation Application

A Java-based simulation engine designed to model the spread and evolution of infectious diseases across interconnected geographical regions. This project simulates epidemiological dynamics, including viral mutations and the impact of public health interventions.

## Key Features

* **Multi-Region Dynamics:** Simulates population movement between neighboring regions with adjustable mobility rates.
* **Complex Viral Modeling:** * Supports both **DNA** and **RNA** viruses.
    * Models biological parameters: incubation, latency, mortality, and infectious periods.
    * **Immunity System:** Tracks temporary or permanent immunity within populations.
* **Mutation Engine:** * **Copy Error Mutations:** RNA viruses can mutate based on total infection volume, creating new viral strains dynamically.
    * **Coincidence Mutations:** Handles viral evolution triggered by regional factors.
* **Containment Strategies:** Implements "Hard" lockdowns (regional isolation) and "Soft" lockdowns (restricted mobility between specific neighbors).
* **Statistical Tracking:** Provides daily data on new infections, active cases, recoveries, and fatalities.

## How It Works

The simulation operates on a turn-based (daily) cycle:
1.  **Data Loading:** Initializes regions and viruses from external configuration files.
2.  **Daily Step:** The aplication is allowed to update the state of every region.
3.  **Spread & Evolution:** The engine calculates new infections based on contact rates and then checks for potential viral mutations.
4.  **Intervention:** Users can trigger or lift lockdowns in real-time to observe the impact on the infection curve.

## Graphical User Interface (JavaFX)

The application features a graphical user interface developed with **JavaFX**, designed for intuitive pandemic simulation management. The visual architecture is organized into three main stages: data configuration, daily simulation, and cumulative data analysis.

---

### 1. Configuration and File Loading
Upon launching the application, a welcome window is displayed where the user must load the necessary configuration files to initialize the simulation:

* **Virus File:** Defines the parameters and characteristics of the pathogens.
* **Region File:** Defines the geographical structure and connections between different regions.
* **Initial State File:** Sets the initial population status and infection data for Day 0.

### 2. Daily Simulation
This window serves as the core control center for the pandemic simulation. It allows the user to perform the following actions:

* **Time Control:** An "Advance Day" button that executes the simulation engine and updates the global state.
* **Lockdown Management:**
    * *No Lockdown:* Normal mobility status.
    * *Soft Lockdown:* Restricts mobility between a primary region and a selected neighboring region.
    * *Hard Lockdown:* Applies a specific percentage-based restriction rate to the selected region.
* **Daily Data Visualization:** * **Text Panel:** Displays key indicators such as infected, sick, deceased, etc.
    * **BarChart:** A graphical representation of historical evolution based on selected criteria (Sick, Immune, Contagious, or Deceased).



### 3. Cumulative Data
A dedicated window for global analysis that allows users to consult the total statistical summary from the start of the simulation for any region and virus combination:

* **Textual Summary:** Total accumulated numbers of infected, sick, and deceased individuals.
* **Statistics Graph:** A quick visual comparison of the total accumulated figures.

---
## Technologies
* **Language:** Java
* **Architecture:** Object-Oriented (Core Logic / Engine)
