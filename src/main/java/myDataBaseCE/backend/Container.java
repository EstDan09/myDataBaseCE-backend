package myDataBaseCE.backend;

public class Container {

    private String data;

    /**
     * Función para agregar valor al contenedor de data
     * @param data el valor que le quiero dar a la data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Función para acceder a la data del contenedor
     * @return data retorna el valor de la data
     */
    public String getData(){
        return data;
    }

}
