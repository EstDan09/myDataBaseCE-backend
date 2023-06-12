package myDataBaseCE.backend;

public class User {

    private String userName;
    private String email;
    private String password;

    /**
     * Función para obtener el nombre del Usuario
     * @param None
     * @return      nombre de usuario
     */
    public String getUserName(){
        return userName;
    }

    /**
     * Función para modificar el nombre del Usuario
     * @param userName nuevo nombre de usuario que deseo
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * Función para obtener el correo del Usuario
     * @param None
     * @return      correo del usuario
     */
    public String getEmail(){
        return email;
    }

    /**
     * Función para obtener el correo del Usuario
     * @param email nuevo correo del usuario que deseeo
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * Función para obtener la contraseña del Usuario
     * @param None
     * @return      contraseña del usuario
     */
    public String getPassword(){
        return password;
    }

    /**
     * Función para cambiar la contraseña del Usuario
     * @param password nueva contraeña del usuario que deseo
     */
    public void setPassword(String password){
        this.password = password;
    }


}
