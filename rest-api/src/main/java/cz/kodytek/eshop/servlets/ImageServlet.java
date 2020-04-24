package cz.kodytek.eshop.servlets;

import javax.annotation.Resource;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("assets/*")
public class ImageServlet extends HttpServlet {
    @Resource(lookup = "java:module/ModuleName")
    private String moduleName;

    @Resource(lookup = "java:app/AppName")
    private String applicationName;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/jpeg");

        try {
            File f = new File(req
                    .getRequestURI()
                    .replace("/" + moduleName + "/assets/", "resources/"));

            System.out.println(f.getPath());

            BufferedImage bi = ImageIO.read(f);
            OutputStream os = resp.getOutputStream();
            ImageIO.write(bi, "jpg", os);
            os.close();
        } catch (IIOException ex) {
            resp.sendError(404);
        }
    }
}
