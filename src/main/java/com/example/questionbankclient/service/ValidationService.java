package com.example.questionbankclient.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.questionbankclient.model.Question;

@Service
public class ValidationService {

	public boolean validateQuestion(Question question) {
		
		return StringUtils.isEmpty(question.getQuestion()) ? false : true ;
	}
	
	public boolean validateParams(String question)
	{
		return StringUtils.isEmpty(question) ? false : true ;
	}
}
