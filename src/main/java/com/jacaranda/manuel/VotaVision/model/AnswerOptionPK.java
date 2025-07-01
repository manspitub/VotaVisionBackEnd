package com.jacaranda.manuel.VotaVision.model;

import java.io.Serializable;
import java.util.Objects;

public class AnswerOptionPK implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long answer; 
    private Long option;
	public AnswerOptionPK() {
		super();
	}
	public AnswerOptionPK(Long answer, Long option) {
		super();
		this.answer = answer;
		this.option = option;
	}
	public Long getAnswer() {
		return answer;
	}
	public void setAnswer(Long answer) {
		this.answer = answer;
	}
	public Long getOption() {
		return option;
	}
	public void setOption(Long option) {
		this.option = option;
	}
	@Override
	public int hashCode() {
		return Objects.hash(answer, option);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnswerOptionPK other = (AnswerOptionPK) obj;
		return Objects.equals(answer, other.answer) && Objects.equals(option, other.option);
	}
	
    
}
