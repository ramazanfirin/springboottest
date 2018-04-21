package com.mastertech.springboottest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.mastertech.springboottest.controller.UserController;
import com.mastertech.springboottest.domain.UserRecord;
import com.mastertech.springboottest.errors.ExceptionTranslator;
import com.mastertech.springboottest.repository.UserRepository;
import com.mastertech.springboottest.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringboottestApplication.class)

public class SpringboottestApplicationTests2 {

	private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CLAZZ = "BBBBBBBBBB";
	
	
	@Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;
	
    @Autowired
    private EntityManager em;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired 
	private UserController userController;
    
	@MockBean
	private UserService userService;
	
    private MockMvc restUseRecordMockMvc;
    
    private UserRecord userRecord;
	
	@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //UserController agitoAdapterResource = new UserController();
        this.restUseRecordMockMvc = MockMvcBuilders.standaloneSetup(userController)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }
	
	public static UserRecord createEntity(EntityManager em) {
		UserRecord userRecord = new UserRecord()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL);
        return userRecord;
    }

    @Before
    public void initTest() {
    	userRecord = createEntity(em);
    }
	
    @Test
    @Transactional
    public void createUserRecord() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();
       
        // Create the City
        restUseRecordMockMvc.perform(post("/api/users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRecord)))
            .andExpect(status().isCreated());

        // Validate the City in the database
        List<UserRecord> cityList = userRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeCreate + 1);
        UserRecord testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getName()).isEqualTo(DEFAULT_NAME);
    }
    
    @Test
    @Transactional
    public void createCityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the City with an existing ID
        userRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUseRecordMockMvc.perform(post("/api/users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRecord)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserRecord> cityList = userRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeCreate);
    }
    
    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRepository.findAll().size();
        // set the field null
        userRecord.setName(null);

        // Create the City, which fails.

        restUseRecordMockMvc.perform(post("/api/users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRecord)))
            .andExpect(status().isBadRequest());

        List<UserRecord> cityList = userRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeTest);
    }
    
    @Test
    @Transactional
    public void getAllCities() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(userRecord);

        // Get all the cityList
        restUseRecordMockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    
    @Test
    @Transactional
    public void getCity() throws Exception {
        // Initialize the database
    	userRepository.saveAndFlush(userRecord);

        // Get the city
    	restUseRecordMockMvc.perform(get("/api/users/{id}", userRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id").value(userRecord.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }
    
    @Test
    @Transactional
    public void getNonExistingCity() throws Exception {
        // Get the city
    	restUseRecordMockMvc.perform(get("/api/users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
    
    @Test
    @Transactional
    public void updateCity() throws Exception {
        // Initialize the database
    	userRepository.saveAndFlush(userRecord);
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the city
        Optional<UserRecord> updatedCity = userRepository.findById(userRecord.getId());
        updatedCity.orElseGet(null).name(UPDATED_NAME);

        restUseRecordMockMvc.perform(put("/api/users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCity.get())))
            .andExpect(status().isOk());

        // Validate the City in the database
        List<UserRecord> cityList = userRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
        UserRecord testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getName()).isEqualTo(UPDATED_NAME);
    }
    
    @Test
    @Transactional
    public void updateNonExistingCity() throws Exception {
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Create the City

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUseRecordMockMvc.perform(put("/api/users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRecord)))
            .andExpect(status().isCreated()).andDo(print());

        // Validate the City in the database
        List<UserRecord> cityList = userRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate + 1);
    }
    
    @Test
    @Transactional
    public void deleteCity() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(userRecord);
        int databaseSizeBeforeDelete = userRepository.findAll().size();

        // Get the city
        restUseRecordMockMvc.perform(delete("/api/users/{id}", userRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserRecord> cityList = userRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeDelete - 1);
    }
    
    
    
	@Test
	public void contextLoads() {
		System.out.println("bitti");
	}

}
