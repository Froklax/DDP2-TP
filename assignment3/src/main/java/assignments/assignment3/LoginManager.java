package assignments.assignment3;

import assignments.assignment3.systemCLI.AdminSystemCLI;
import assignments.assignment3.systemCLI.CustomerSystemCLI;
import assignments.assignment3.systemCLI.UserSystemCLI;

public class LoginManager {
    // Atribut class LoginManager
    private final AdminSystemCLI adminSystem;
    private final CustomerSystemCLI customerSystem;

    // Constructor class LoginManager
    public LoginManager(AdminSystemCLI adminSystem, CustomerSystemCLI customerSystem) {
        this.adminSystem = adminSystem;
        this.customerSystem = customerSystem;
    }

    // Method untuk mendapatkan system yang sesuai dengan role customer
    public UserSystemCLI getSystem(String role){
        if(role.equals("Customer")){
            return customerSystem;
        }else{
            return adminSystem;
        }
    }
}
