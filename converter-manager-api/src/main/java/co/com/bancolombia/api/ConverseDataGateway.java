package co.com.bancolombia.api;



public interface ConverseDataGateway {
    <T> T xmlToObject(String xml, String templateCode, Class<T> target);

    String jsonToXml(String json, String templateCode, Object... context) ;
}
