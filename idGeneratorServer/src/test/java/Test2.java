import com.mike.idgenerator.utils.IdGeneratorServiceBuilder;

public class Test2 {
    public static void main(String[] args) {
        IdGeneratorServiceBuilder idGeneratorServiceBuilder = new IdGeneratorServiceBuilder();
        idGeneratorServiceBuilder.option(IdGeneratorServiceBuilder.PORT,8080);
        idGeneratorServiceBuilder.build();
    }
}
