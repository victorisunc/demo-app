package de.mobile.api.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorMessage {
  private int statusCode;
  private Date timestamp;
  private String message;
  private String description;
  private List<String> errors;
}
