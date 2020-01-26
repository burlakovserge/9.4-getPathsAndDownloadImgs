import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException{
        String downloadURL = "https://lenta.ru/";
        String downloadTo = "F:/new folder";

        Document doc = null;
        try {
            doc = Jsoup.connect(downloadURL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements srcWithImgAttr = doc.select("img[src]"); // список абсолютных путей изображений
        downloadAllImg(getImgSources(srcWithImgAttr), downloadTo); // скачать все изображения в папку
    }

    private static List<String> getImgSources(Elements srcWithImgAttr) {

        List<String> sourcePic = new ArrayList<>();
        for (Element element : srcWithImgAttr) {
            String e = element.attributes().toString();
            String[] params = e.substring(e.indexOf("src") + 5).replace("\"", "").split("\\s");
            if (params.length == 3) {
                sourcePic.add(params[0]);
            }
        }
        return sourcePic;
    }

    private static void downloadAllImg(List<String> paths, String folder) throws IOException{
        File downloadFolder = new File(folder);
        if(downloadFolder.exists()) {
            for (String path : paths) {
                URL url = new URL(path);
                InputStream inputStream = url.openStream();
                String fileName = path.substring(path.lastIndexOf("/"));
                Files.copy(inputStream, new File(folder + "/" + fileName).toPath());
            }
        } else System.out.println("Папка для загрузки с таким именем не существует");
    }
}