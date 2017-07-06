/*
 * Copyright 2017 The Mifos Initiative.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mifos.reporting.api.v1.client;

import io.mifos.core.api.annotation.ThrowsException;
import io.mifos.core.api.annotation.ThrowsExceptions;
import io.mifos.core.api.util.CustomFeignClientsConfiguration;
import io.mifos.core.lang.ServiceException;
import io.mifos.reporting.api.v1.PermittableGroupIds;
import io.mifos.reporting.api.v1.domain.ReportDefinition;
import io.mifos.reporting.api.v1.domain.ReportPage;
import io.mifos.reporting.api.v1.domain.ReportRequest;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@SuppressWarnings("unused")
@FeignClient(value="reporting-v1", path="/reporting/v1", configuration = CustomFeignClientsConfiguration.class)
public interface ReportManager {

  @RequestMapping(
          value = "/categories",
          method = RequestMethod.GET,
          produces = MediaType.ALL_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE
  )
  List<String> fetchCategories();

  @RequestMapping(
          value = "/categories/{category}",
          method = RequestMethod.GET,
          produces = MediaType.ALL_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE)
  @ThrowsExceptions({
      @ThrowsException(status = HttpStatus.NOT_FOUND, exception = ReportNotFoundException.class),
  })
  List<ReportDefinition> fetchReportDefinitions(@PathVariable("category") final String category);

  @RequestMapping(
      value = "/categories/{category}/reports/{identifier}",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ThrowsExceptions({
      @ThrowsException(status = HttpStatus.NOT_FOUND, exception = ReportNotFoundException.class),
      @ThrowsException(status = HttpStatus.BAD_REQUEST, exception = ReportParameterValidationException.class)
  })
  ReportPage generateReport(@PathVariable("category") final String category,
                            @PathVariable("identifier") final String identifier,
                            @RequestBody final ReportRequest reportRequest,
                            @RequestParam(value = "pageIndex", required = false) final Integer pageIndex,
                            @RequestParam(value = "size", required = false) final Integer size);

  @RequestMapping(
      value = "categories/{category}/definitions/{identifier}",
      method = RequestMethod.GET,
      produces = MediaType.ALL_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @ThrowsExceptions({
      @ThrowsException(status = HttpStatus.NOT_FOUND, exception = ReportNotFoundException.class)
  })
  ReportDefinition findReportDefinition(@PathVariable("category") final String category,
                                        @PathVariable("identifier") final String identifier);
}
