/*
 * Copyright 2016 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.castlemock.service.mock.rest.project;

import com.castlemock.model.core.ServiceResult;
import com.castlemock.model.core.ServiceTask;
import com.castlemock.model.mock.rest.domain.RestApplication;
import com.castlemock.model.mock.rest.domain.RestApplicationTestBuilder;
import com.castlemock.model.mock.rest.domain.RestMethod;
import com.castlemock.model.mock.rest.domain.RestMethodTestBuilder;
import com.castlemock.model.mock.rest.domain.RestMockResponse;
import com.castlemock.model.mock.rest.domain.RestMockResponseTestBuilder;
import com.castlemock.model.mock.rest.domain.RestResource;
import com.castlemock.model.mock.rest.domain.RestResourceTestBuilder;
import com.castlemock.repository.rest.project.RestApplicationRepository;
import com.castlemock.repository.rest.project.RestMethodRepository;
import com.castlemock.repository.rest.project.RestMockResponseRepository;
import com.castlemock.repository.rest.project.RestResourceRepository;
import com.castlemock.service.mock.rest.project.input.DeleteRestApplicationInput;
import com.castlemock.service.mock.rest.project.output.DeleteRestApplicationOutput;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
public class DeleteRestApplicationServiceTest {

    @Mock
    private RestApplicationRepository applicationRepository;

    @Mock
    private RestResourceRepository resourceRepository;

    @Mock
    private RestMethodRepository methodRepository;

    @Mock
    private RestMockResponseRepository mockResponseRepository;

    @InjectMocks
    private DeleteRestApplicationService service;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcess(){
        final String projectId = "ProjectId";
        final String applicationId = "ApplicationId";
        final RestApplication application = RestApplicationTestBuilder.builder().build();
        final RestResource resource = RestResourceTestBuilder.builder().build();
        final RestMethod method = RestMethodTestBuilder.builder().build();
        final RestMockResponse mockResponse = RestMockResponseTestBuilder.builder().build();

        Mockito.when(applicationRepository.delete(Mockito.any())).thenReturn(application);
        Mockito.when(resourceRepository.findWithApplicationId(applicationId)).thenReturn(Arrays.asList(resource));
        Mockito.when(methodRepository.findWithResourceId(resource.getId())).thenReturn(Arrays.asList(method));
        Mockito.when(mockResponseRepository.findWithMethodId(method.getId())).thenReturn(Arrays.asList(mockResponse));

        final DeleteRestApplicationInput input = DeleteRestApplicationInput.builder()
                .restProjectId(projectId)
                .restApplicationId(applicationId)
                .build();
        final ServiceTask<DeleteRestApplicationInput> serviceTask = new ServiceTask<DeleteRestApplicationInput>(input);
        final ServiceResult<DeleteRestApplicationOutput> serviceResult = service.process(serviceTask);

        Mockito.verify(applicationRepository, Mockito.times(1)).delete(applicationId);
        Mockito.verify(resourceRepository, Mockito.times(1)).delete(resource.getId());
        Mockito.verify(methodRepository, Mockito.times(1)).delete(method.getId());
        Mockito.verify(mockResponseRepository, Mockito.times(1)).delete(mockResponse.getId());

        Mockito.verify(resourceRepository, Mockito.times(1)).findWithApplicationId(applicationId);
        Mockito.verify(methodRepository, Mockito.times(1)).findWithResourceId(resource.getId());
        Mockito.verify(mockResponseRepository, Mockito.times(1)).findWithMethodId(method.getId());
    }
}
