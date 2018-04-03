package com.docscarsexample;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.docscarsexample.domain.Car;
import com.docscarsexample.repository.CarRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DocsCarsExampleApplication.class)
@WebAppConfiguration
public class DocsCarsExampleApplicationTests {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    
    @After
    public void resetDb() {
    	carRepository.deleteAll();
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation)
//                        .uris()
//                            .withScheme("https://")
//                            .withHost("example.com")
//                            .withPort(443)
                )
                .build();
    }

    @Test
    public void index() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/")).andExpect(status().isOk())
                .andDo(document("index-links", links(
                        linkWithRel("carapi:cars").description("The <<cars,Car resources>>"),
                        linkWithRel("curies").description("Curies for documentation"),
                        linkWithRel("profile").description("The ALPS profile for the service")
                        ),
                        responseFields(fieldWithPath("_links").description("<<index-links-links,Links>> to other resources"))
                ));
    }
    
    
    @Test
    public void cars() throws Exception {
    	
    	Car car1 = new Car();
    	car1.setIdCar(new Long(1));
    	car1.setIdUser(new Long(1));
    	car1.setIsDefault(true);
    	car1.setCreatedAt(new Date());
    	car1.setBrand("Peugeot");
    	car1.setModel("206");
    	
    	Car car2 = new Car();
    	car2.setIdCar(new Long(2));
    	car2.setIdUser(new Long(2));
    	car2.setIsDefault(true);
    	car2.setCreatedAt(new Date());
    	car2.setBrand("Renault");
    	car2.setModel("Clio");  	
    	
    	carRepository.save(car1); 
    	carRepository.save(car2);    	
    	
        mockMvc.perform(RestDocumentationRequestBuilders.get("/cars")).andExpect(status().isOk())
                .andDo(document("car-list", links(
                        linkWithRel("self").ignored(),
                        linkWithRel("profile").description("The ALPS profile for the service"),
                        linkWithRel("curies").ignored()
                        ),
                        responseFields(
                                fieldWithPath("_embedded.carapi:cars").description("A list of <<cars, Car resources>>"),
                                fieldWithPath("_links").description("<<cars-links,Links>> to other resources")
                        )

                ));
    }

    @Test
    public void createCar() throws Exception {
    	
    	Car car = new Car();
    	car.setIdCar(new Long(1));
    	car.setIdUser(new Long(1));
    	car.setIsDefault(true);
    	car.setCreatedAt(new Date());
    	car.setBrand("Peugeot");
    	car.setModel("206");

        mockMvc.perform(post("/cars")
        		.contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isCreated())
                .andDo(document("car-create",
                        requestFields(
                        		fieldWithPath("id").description("id of your car"),
                        		fieldWithPath("id_car").description("another id of your car"),
                        		fieldWithPath("id_user").description("id of the user"),
                        		fieldWithPath("is_default").description("is this the default car?"),
                        		fieldWithPath("created_at").description("creation date"),
                                fieldWithPath("brand").description("Brand of your car"),
                                fieldWithPath("model").description("Model of your car")   
                                        .attributes(Attributes.key("constraints").value("must provide a `name` property")))));
    }
    
    
    @Test
    public void getOneCar() throws Exception {
	
    	Car car = new Car();
    	car.setIdCar(new Long(2));
    	car.setIdUser(new Long(2));
    	car.setIsDefault(true);
    	car.setCreatedAt(new Date());
    	car.setBrand("Peugeot1");
    	car.setModel("2061");
    	
    	carRepository.save(car);   	
    	
    	mockMvc.perform(RestDocumentationRequestBuilders.get("/cars/{id}", car.getId())
    		.contentType(MediaType.APPLICATION_JSON))
    		.andDo(print())
    		.andExpect(status().isOk())
	        .andDo(document("car-get", links(
	                linkWithRel("self").ignored(),
	                linkWithRel("carapi:car").description("The <<cars, Car resource>> itself"),
	                linkWithRel("curies").ignored()
	                ),
	                responseFields(
	                		fieldWithPath("id_car").description("another id of your car"),
	                		fieldWithPath("id_user").description("id of the user"),
	                		fieldWithPath("is_default").description("is this the default car?"),
	                		fieldWithPath("created_at").description("creation date"),
	                        fieldWithPath("brand").description("Brand of your car"),
	                        fieldWithPath("model").description("Model of your car"),
	                        fieldWithPath("_links").description("<<car-links,Links>> to other resources")
	                )
	        ));
    }
    
    @Test
    public void removeOneCar() throws Exception {
    	
    	Car car = new Car();
    	car.setIdCar(new Long(2));
    	car.setIdUser(new Long(2));
    	car.setIsDefault(true);
    	car.setCreatedAt(new Date());
    	car.setBrand("Peugeot1");
    	car.setModel("2061");
    	
    	carRepository.save(car);   	

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/cars/{id}", car.getId())).andExpect(status().isNoContent())
                .andDo(document("car-delete"));
    }    

}
