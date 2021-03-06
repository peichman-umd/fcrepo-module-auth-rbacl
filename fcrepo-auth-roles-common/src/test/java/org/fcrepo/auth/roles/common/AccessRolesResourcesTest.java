/*
 * Licensed to DuraSpace under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 *
 * DuraSpace licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fcrepo.auth.roles.common;

import static org.fcrepo.http.commons.test.util.TestHelpers.getUriInfoImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.ws.rs.core.UriInfo;

import org.fcrepo.kernel.api.FedoraTypes;
import org.fcrepo.kernel.api.RdfLexicon;
import org.fcrepo.kernel.api.identifiers.IdentifierConverter;
import org.fcrepo.kernel.api.models.FedoraResource;
import org.fcrepo.kernel.modeshape.FedoraResourceImpl;
import org.fcrepo.kernel.modeshape.rdf.impl.DefaultIdentifierTranslator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author bbpennel
 * @since Feb 17, 2014
 */
public class AccessRolesResourcesTest {

    private IdentifierConverter<Resource, FedoraResource> graphSubjects;

    @Mock
    private FedoraResourceImpl fedoraResource;

    @Mock
    private Node resourceNode;

    @Mock
    private Session mockSession;

    private Model model;

    private UriInfo uriInfo;

    private AccessRolesResources resources;

    private String pathString;

    @Before
    public void setUp() throws RepositoryException {
        initMocks(this);

        resources = new AccessRolesResources();

        pathString = "path";
        model = ModelFactory.createDefaultModel();
        graphSubjects = new DefaultIdentifierTranslator(mockSession);

        when(fedoraResource.getNode()).thenReturn(resourceNode);
        when(resourceNode.getPath()).thenReturn("/" + pathString);

        uriInfo = getUriInfoImpl();
    }

    @Test
    public void testCreateModelForNonFedoraResource()
            throws RepositoryException {

        when(fedoraResource.getPath()).thenReturn("/" + pathString);
        when(resourceNode.isNodeType(eq(FedoraTypes.FEDORA_RESOURCE)))
                .thenReturn(false);

        final Model model =
                resources.createModelForResource(fedoraResource, uriInfo,
                        graphSubjects);

        assertTrue("Model should be an empty default model", model.isEmpty());
    }

    @Test
    public void testCreateModelForResource() throws RepositoryException {

        when(fedoraResource.hasType(eq(FedoraTypes.FEDORA_RESOURCE)))
                .thenReturn(true);

        when(fedoraResource.getPath()).thenReturn("/" + pathString);

        final Model model =
                resources.createModelForResource(fedoraResource, uriInfo,
                        graphSubjects);

        assertFalse("Model should not be empty", model.isEmpty());

        final ResIterator resIterator =
                model.listResourcesWithProperty(RdfLexicon.HAS_ACCESS_ROLES_SERVICE);

        assertTrue(
                "No resources with property HAS_ACCESS_ROLES_SERVICE in model",
                resIterator.hasNext());

        final Resource addedResource = resIterator.next();

        assertEquals(
                "Resource localname should match URI of provided resource",
                pathString, addedResource.getLocalName());
    }
}
