package com.mycompany.blog.web.rest;

import com.mycompany.blog.Application;
import com.mycompany.blog.domain.Entry;
import com.mycompany.blog.repository.EntryRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EntryResource REST controller.
 *
 * @see EntryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EntryResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_CONTENT = "SAMPLE_TEXT";
    private static final String UPDATED_CONTENT = "UPDATED_TEXT";

    private static final DateTime DEFAULT_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.print(DEFAULT_DATE);

    @Inject
    private EntryRepository entryRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restEntryMockMvc;

    private Entry entry;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntryResource entryResource = new EntryResource();
        ReflectionTestUtils.setField(entryResource, "entryRepository", entryRepository);
        this.restEntryMockMvc = MockMvcBuilders.standaloneSetup(entryResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        entry = new Entry();
        entry.setName(DEFAULT_NAME);
        entry.setContent(DEFAULT_CONTENT);
        entry.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createEntry() throws Exception {
        int databaseSizeBeforeCreate = entryRepository.findAll().size();

        // Create the Entry

        restEntryMockMvc.perform(post("/api/entrys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isCreated());

        // Validate the Entry in the database
        List<Entry> entrys = entryRepository.findAll();
        assertThat(entrys).hasSize(databaseSizeBeforeCreate + 1);
        Entry testEntry = entrys.get(entrys.size() - 1);
        assertThat(testEntry.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEntry.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testEntry.getDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = entryRepository.findAll().size();
        // set the field null
        entry.setName(null);

        // Create the Entry, which fails.

        restEntryMockMvc.perform(post("/api/entrys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isBadRequest());

        List<Entry> entrys = entryRepository.findAll();
        assertThat(entrys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = entryRepository.findAll().size();
        // set the field null
        entry.setContent(null);

        // Create the Entry, which fails.

        restEntryMockMvc.perform(post("/api/entrys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isBadRequest());

        List<Entry> entrys = entryRepository.findAll();
        assertThat(entrys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = entryRepository.findAll().size();
        // set the field null
        entry.setDate(null);

        // Create the Entry, which fails.

        restEntryMockMvc.perform(post("/api/entrys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isBadRequest());

        List<Entry> entrys = entryRepository.findAll();
        assertThat(entrys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEntrys() throws Exception {
        // Initialize the database
        entryRepository.saveAndFlush(entry);

        // Get all the entrys
        restEntryMockMvc.perform(get("/api/entrys"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entry.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)));
    }

    @Test
    @Transactional
    public void getEntry() throws Exception {
        // Initialize the database
        entryRepository.saveAndFlush(entry);

        // Get the entry
        restEntryMockMvc.perform(get("/api/entrys/{id}", entry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(entry.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingEntry() throws Exception {
        // Get the entry
        restEntryMockMvc.perform(get("/api/entrys/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntry() throws Exception {
        // Initialize the database
        entryRepository.saveAndFlush(entry);

        int databaseSizeBeforeUpdate = entryRepository.findAll().size();

        // Update the entry
        entry.setName(UPDATED_NAME);
        entry.setContent(UPDATED_CONTENT);
        entry.setDate(UPDATED_DATE);


        restEntryMockMvc.perform(put("/api/entrys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isOk());

        // Validate the Entry in the database
        List<Entry> entrys = entryRepository.findAll();
        assertThat(entrys).hasSize(databaseSizeBeforeUpdate);
        Entry testEntry = entrys.get(entrys.size() - 1);
        assertThat(testEntry.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEntry.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testEntry.getDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteEntry() throws Exception {
        // Initialize the database
        entryRepository.saveAndFlush(entry);

        int databaseSizeBeforeDelete = entryRepository.findAll().size();

        // Get the entry
        restEntryMockMvc.perform(delete("/api/entrys/{id}", entry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Entry> entrys = entryRepository.findAll();
        assertThat(entrys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
