package cz.kodytek.eshop.servlets;

import io.swagger.jaxrs.config.BeanConfig;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


public class SwaggerServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException { // TODO: Fix not working
        super.init(config);
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/api");
        beanConfig.setResourcePackage("cz.kodytek.eshop.api.rest.resources");
        beanConfig.setScan(true);
    }
}
