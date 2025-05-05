package edu.gmu.cs321;
import com.cs321.Workflow;

/**
 * Hello world!
 *
 */
public class App 
{public static void main(String[] args) {
    System.out.println("Workflow API Demo");
    System.out.println("=================");
    
    // Create a workflow instance
    Workflow workflow = null;
    
    try {
        // Initialize the Workflow component
        workflow = new Workflow();
        System.out.println("Connected to the workflow database successfully.");
        
        // 1. Demonstrate adding workflow items with different statuses
        System.out.println("\n1. Adding workflow items:");
        
        // Add items for Review status
        int result1 = workflow.AddWFItem(101, "Review");
        System.out.println("  - Adding item ID 101 to Review: " + getStatusMessage(result1));
        
        int result2 = workflow.AddWFItem(102, "Review");
        System.out.println("  - Adding item ID 102 to Review: " + getStatusMessage(result2));
        
        // Add items for Approve status
        int result3 = workflow.AddWFItem(201, "Approve");
        System.out.println("  - Adding item ID 201 to Approve: " + getStatusMessage(result3));
        
        // Add items for Supervisor status
        int result4 = workflow.AddWFItem(301, "Supervisor");
        System.out.println("  - Adding item ID 301 to Supervisor: " + getStatusMessage(result4));
        
        // Try adding an item with an invalid status
        int result5 = workflow.AddWFItem(401, "Invalid");
        System.out.println("\n  - Adding item ID 401 with invalid status: " + getStatusMessage(result5));
        
        // Try adding a duplicate item
        int result6 = workflow.AddWFItem(101, "Review");
        System.out.println("  - Trying to add duplicate item ID 101: " + getStatusMessage(result6));
        
        // 2. Demonstrate retrieving workflow items
        System.out.println("\n2. Retrieving workflow items:");
        
        // Retrieve items from Review status
        System.out.println("  - Items in Review status:");
        int reviewItem = workflow.GetNextWFItem("Review");
        while (reviewItem > 0) {
            System.out.println("    Retrieved item ID: " + reviewItem);
            reviewItem = workflow.GetNextWFItem("Review");
        }
        
        // Retrieve items from Approve status
        System.out.println("  - Items in Approve status:");
        int approveItem = workflow.GetNextWFItem("Approve");
        while (approveItem > 0) {
            System.out.println("    Retrieved item ID: " + approveItem);
            approveItem = workflow.GetNextWFItem("Approve");
        }
        
        // Retrieve items from Supervisor status
        System.out.println("  - Items in Supervisor status:");
        int supervisorItem = workflow.GetNextWFItem("Supervisor");
        while (supervisorItem > 0) {
            System.out.println("    Retrieved item ID: " + supervisorItem);
            supervisorItem = workflow.GetNextWFItem("Supervisor");
        }
        
        // Try retrieving from an invalid status
        System.out.println("  - Trying to retrieve from invalid status:");
        int invalidItem = workflow.GetNextWFItem("Invalid");
        System.out.println("    Result: " + getRetrieveStatusMessage(invalidItem));
        
        // 3. Demonstrate workflow processing scenario
        System.out.println("\n3. Complete workflow processing scenario:");
        
        // Add new items for the scenario
        System.out.println("  - Adding new items to the workflow");
        workflow.AddWFItem(601, "Review");
        workflow.AddWFItem(602, "Review");
        workflow.AddWFItem(603, "Review");
        
        // Process items through the review stage
        System.out.println("  - Processing items through Review stage");
        int itemToProcess = workflow.GetNextWFItem("Review");
        while (itemToProcess > 0) {
            System.out.println("    Processing item ID: " + itemToProcess + " (Review)");
            // After review, move to Approve stage
            workflow.AddWFItem(itemToProcess, "Approve");
            itemToProcess = workflow.GetNextWFItem("Review");
        }
        
        // Process items through the approve stage
        System.out.println("  - Processing items through Approve stage");
        itemToProcess = workflow.GetNextWFItem("Approve");
        while (itemToProcess > 0) {
            System.out.println("    Processing item ID: " + itemToProcess + " (Approve)");
            // After approval, move to Supervisor stage
            workflow.AddWFItem(itemToProcess, "Supervisor");
            itemToProcess = workflow.GetNextWFItem("Approve");
        }
        
        // Final supervisor review
        System.out.println("  - Final supervisor review");
        itemToProcess = workflow.GetNextWFItem("Supervisor");
        while (itemToProcess > 0) {
            System.out.println("    Supervisor approved item ID: " + itemToProcess);
            itemToProcess = workflow.GetNextWFItem("Supervisor");
        }
        
    } catch (Exception e) {
        System.out.println("\nError in workflow operations:");
        e.printStackTrace();
    } finally {
        // Always close the connection when done
        if (workflow != null) {
            try {
                workflow.closeConnection();
                System.out.println("\nWorkflow database connection closed successfully.");
            } catch (Exception e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    System.out.println("\nWorkflow API Demo completed.");
}

/**
 * Helper method to convert the status code to a readable message
 */
private static String getStatusMessage(int statusCode) {
    switch (statusCode) {
        case 0:
            return "Success";
        case -1:
            return "Invalid NextStep (must be Review, Approve, or Supervisor)";
        case -2:
            return "FormID already exists or is invalid";
        default:
            return "Unknown status code: " + statusCode;
    }
}

/**
 * Helper method to convert the retrieve status code to a readable message
 */
private static String getRetrieveStatusMessage(int statusCode) {
    if (statusCode > 0) {
        return "Retrieved item ID: " + statusCode;
    } else if (statusCode == -1) {
        return "Invalid NextStep (must be Review, Approve, or Supervisor)";
    } else if (statusCode == -3) {
        return "No items available with the specified status";
    } else {
        return "Unknown status code: " + statusCode;
    }
}

}
