package myDataBaseCE.backend;

import com.fazecast.jSerialComm.*;
public class SerialCommunication {
    private static SerialPort serialPort;

    private static StringBuilder concatenatedString = new StringBuilder();

    public static StringBuilder getConcatenatedString() {
        if (concatenatedString != null) {
            String data = concatenatedString.toString().replaceAll("\\r\\n|\\r|\\n", "");
            return new StringBuilder(data);
        } else {
            return new StringBuilder();
        }
    }
    static String contraseña = "";

    public static void enviarDato(String dato) {

        serialPort = SerialPort.getCommPort("COM8"); // Reemplaza "COM3" con el puerto serial correcto

        serialPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);

        // Abre el puerto serial
        if (serialPort.openPort()) {
            System.out.println("Puerto serial abierto correctamente.");

            // Espera un tiempo antes de enviar el dato
            try {
                Thread.sleep(2000); // Espera 2 segundos (puedes ajustar este valor según tus necesidades)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Envia el dato
            byte[] buffer = dato.getBytes();
            serialPort.writeBytes(buffer, buffer.length);
            System.out.println("Dato enviado después de cerrar el puerto: " + dato);

            // Cierra el puerto serial
            serialPort.closePort();

            System.out.println("Puerto serial cerrado.");
        } else {
            System.out.println("No se pudo abrir el puerto serial.");
        }
    }

    public static String recibirDato(){
        // Obtén una instancia de la librería jSerialComm
        serialPort = SerialPort.getCommPort("COM8"); // Reemplaza "COM3" con el puerto serial correcto

        // Configura los parámetros de la conexión serial (baud rate, bits de datos, paridad, etc.)
        serialPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);

        // Abre el puerto serial
        if (serialPort.openPort()) {
            System.out.println("Puerto serial abierto correctamente.");

            // Crea un hilo para recibir datos continuamente
            Thread thread = new Thread(() -> {
                long startTime = System.currentTimeMillis();
                while (serialPort.isOpen()) {
                    // Verifica si ha pasado cierto tiempo y detiene el hilo de recepción
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    if (elapsedTime >= 10000) { // Reemplaza TIEMPO_LIMITE con el tiempo deseado en milisegundos
                        break;
                    }

                    // Verifica si hay datos disponibles para leer
                    if (serialPort.bytesAvailable() > 0) {
                        // Lee los datos del puerto serial
                        byte[] buffer = new byte[serialPort.bytesAvailable()];
                        int numRead = serialPort.readBytes(buffer, buffer.length);

                        // Convierte los datos leídos en una cadena de texto
                        String data = new String(buffer, 0, numRead);

                        // Concatena el string recibido al string acumulado
                        concatenatedString.append(data);
                        contraseña = String.valueOf(getConcatenatedString());
                    }
                }
            });

            // Inicia el hilo de recepción de datos
            thread.start();

            // Espera a que el hilo termine antes de continuar
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Detiene el hilo de recepción de datos si aún está en ejecución
            if (thread.isAlive()) {
                thread.interrupt();
            }

            // Cierra el puerto serial
            serialPort.closePort();

            System.out.println("Puerto serial cerrado.");

            // Devuelve la contraseña obtenida
            return contraseña;
        } else {
            System.out.println("No se pudo abrir el puerto serial.");
        }

        return null;
    }
}
