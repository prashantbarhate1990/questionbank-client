package com.example.questionbankclient.contoller;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.questionbankclient.model.Question;
import com.example.questionbankclient.model.Questions;
import com.example.questionbankclient.service.ValidationService;

@RestController
@RequestMapping("/nvquestionbank")
public class QuestionBankClientController {

	@Value("${questionbank.service.geturi}")
	private String geturi ;
	
	@Value("${questionbank.service.posturi}")
	private String posturi ;
	
	
	@Value("${questionbank.service.updateuri}")
	private String updateuri ;
	
	Logger logger = LogManager.getLogger(QuestionBankClientController.class);
	
	@Autowired
	ValidationService validationService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/questions", method=RequestMethod.GET, produces="application/json") 
	public ResponseEntity<Questions> getQuestions() {
	
		logger.info("Inside getQuestion method of client");
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    ResponseEntity<List> res =  restTemplate.getForEntity(geturi, List.class);
	    logger.info("questions returned from question service");
	    Questions questions = new Questions();
	    questions.setQuestion(res.getBody());
	     
	    return new ResponseEntity<> (questions, res.getStatusCode());
	 }
	
	@RequestMapping(value="/addquestion", method=RequestMethod.POST, produces="application/json")
	public ResponseEntity<String> addQuestion(@RequestBody  Question question)
	{
		boolean validate = validationService.validateQuestion(question);
		
		if(!validate) {
			logger.error("Question field is blank or null");
			return new ResponseEntity<> ("Question field is missing/invalid", HttpStatus.BAD_REQUEST);
		}
		
		logger.info("Input parameters validation complete");
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity<Question> entity = new HttpEntity<>(question, headers);
	    ResponseEntity<String> res = restTemplate.exchange(
	         posturi, HttpMethod.POST, entity, String.class);
	    logger.info("Response returned from question service");
	    return res;
	}
	
	@RequestMapping(value="/updatequestion", method=RequestMethod.POST, produces="application/json")
	public ResponseEntity<String> updateQuestion(@RequestBody Question question)
	{
		boolean validate = validationService.validateQuestion(question);
		
		if(!validate) {
			logger.error("Question field is blank or null");
			return new ResponseEntity<> ("Question field is missing/invalid", HttpStatus.BAD_REQUEST);
		}
		
		logger.info("Input parameters validation complete");
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity<Question> entity = new HttpEntity<>(question, headers);
	    ResponseEntity<String> res;
		try {
			res = restTemplate.exchange(
			     updateuri, HttpMethod.POST, entity, String.class);
		} catch (RestClientException e) {
			return new ResponseEntity<> ("Request is invalid. Please provide valid ID", HttpStatus.BAD_REQUEST);
		}
	    logger.info("Response returned from question service");
	    return res;
	}
}
