public class Arista {
    public String destino;
    public double costo; // Peso de la ruta (tiempo/costo)

    public Arista(String destino, double costo) {
        this.destino = destino;
        this.costo = costo;
    }
}