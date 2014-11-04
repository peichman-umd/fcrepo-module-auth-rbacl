/**
 * Copyright 2014 DuraSpace, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fcrepo.auth.roles.basic.integration;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.fcrepo.auth.roles.common.integration.RolesFadTestObjectBean;

import org.apache.http.client.ClientProtocolException;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Verifies that role for writers is properly enforced.
 *
 * @author Scott Prater
 * @author Gregory Jansen
 */
public class BasicRolesWriterIT extends AbstractBasicRolesIT {

    private final static String TESTDS = "writertestds";

    private final static String TESTCHILD = "writertestchild";

    @Override
    protected List<RolesFadTestObjectBean> getTestObjs() {
        return test_objs;
    }

    /* Public object, one open datastream */
    @Test
    public void testWriterCanReadOpenObj()
            throws ClientProtocolException, IOException {
        assertEquals("Writer cannot read testparent1!", OK.getStatusCode(),
                canRead("examplewriter", testParent1, true));
    }

    @Test
    public void testWriterCanWriteDatastreamOnOpenObj()
            throws ClientProtocolException, IOException {
        assertEquals(
"Writer cannot write datastream to testparent1!", CREATED
                .getStatusCode(), canAddDS("examplewriter", testParent1,
                        TESTDS, true));
    }

    @Test
    public void testWriterCanAddChildToOpenObj()
            throws ClientProtocolException, IOException {
        assertEquals(
"Writer cannot add child to testparent1!", CREATED
                .getStatusCode(), canAddChild("examplewriter", testParent1,
                        TESTCHILD, true));
    }

    @Test
    public void testWriterCannotAddACLToOpenObj()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add an ACL to testparent1!",
                FORBIDDEN
                .getStatusCode(), canAddACL("examplewriter", testParent1,
                        "everyone", "admin", true));
    }

    /* Public object, one open datastream, one restricted datastream */
    /* object */
    @Test
    public void
    testWriterCanReadOpenObjWithRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals("Writer cannot read testparent2!", OK.getStatusCode(),
                canRead("examplewriter", testParent2, true));
    }

    /* open datastream */
    @Test
    public void testWriterCanReadOpenObjPublicDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
"Writer cannot read datastream testparent2/tsp1_data!", OK
                .getStatusCode(), canRead("examplewriter",
                        testParent2 + "/" + tsp1Data,
                        true));
    }

    @Test
    public void
    testWriterCanUpdateOpenObjPublicDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
"Writer cannot update datastream testparent2/tsp1_data!",
                NO_CONTENT
                .getStatusCode(), canUpdateDS("examplewriter",
                        testParent2,
                        tsp1Data, true));
    }

    @Test
    public void testWriterCannotAddACLToOpenObjPublicDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add an ACL to datastream testparent2/tsp1_data!",
                FORBIDDEN.getStatusCode(), canAddACL("examplewriter",
                        testParent2 + "/" + tsp1Data, "everyone", "admin", true));
    }

    /* restricted datastream */
    @Test
    public void testWriterCanReadOpenObjRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot read restricted datastream testparent2/tsp2_data!",
                OK.getStatusCode(), canRead("examplewriter",
                        testParent2 + "/" + tsp2Data, true));
    }

    @Test
    public void testWriterCanUpdateOpenObjRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot update restricted datastream testparent2/tsp2_data!",
                NO_CONTENT.getStatusCode(), canUpdateDS("examplewriter",
                        testParent2,
                        tsp2Data, true));
    }

    @Test
    public void testWriterCannotAddACLToOpenObjRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add an ACL to restricted datastream testparent2/tsp2_data!",
                FORBIDDEN.getStatusCode(), canAddACL("examplewriter",
                        testParent2 + "/" + tsp2Data, "everyone", "admin", true));
    }

    /* Child object (inherits ACL), one open datastream */
    @Test
    public void testWriterCanReadInheritedACLChildObj()
            throws ClientProtocolException, IOException {
        assertEquals(
"Writer cannot read testparent1/testchild1NoACL!", OK
                .getStatusCode(), canRead("examplewriter",
                        testParent1 + "/" + testChild1NoACL,
                        true));
    }

    @Test
    public void testWriterCanWriteDatastreamOnInheritedACLChildObj()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot write datastream to testparent1/testchild1NoACL!",
                Status.CREATED.getStatusCode(), canAddDS("examplewriter",
                        testParent1 + "/" + testChild1NoACL, TESTDS, true));
    }

    @Test
    public void testWriterCanAddChildToInheritedACLChildObj()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot add child to testparent1/testchild1NoACL!",
                Status.CREATED.getStatusCode(), canAddChild("examplewriter",
                        testParent1 + "/" + testChild1NoACL, TESTCHILD, true));
    }

    @Test
    public void testWriterCannotAddACLToInheritedACLChildObj()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add an ACL to testparent1/testchild1NoACL!",
                FORBIDDEN.getStatusCode(), canAddACL("examplewriter",
                        testParent1 + "/" + testChild1NoACL, "everyone", "admin",
                        true));
    }

    @Test
    public void testWriterCanReadInheritedACLChildObjPublicDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot read datastream testparent1/testchild1NoACL/tsc1_data!",
                OK.getStatusCode(), canRead("examplewriter",
                        testParent1 + "/" + testChild1NoACL + "/" + tsc1Data, true));
    }

    @Test
    public void testWriterCanUpdateInheritedACLChildObjPublicDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot update datastream testparent1/testchild1NoACL/tsc1_data!",
                NO_CONTENT.getStatusCode(), canUpdateDS("examplewriter",
                        testParent1 + "/" + testChild1NoACL, tsc1Data, true));
    }

    @Test
    public
    void testWriterCannotAddACLToInheritedACLChildObjPublicDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add an ACL to datastream testparent1/testchild1NoACL/tsc1_data!",
                FORBIDDEN.getStatusCode(), canAddACL("examplewriter",
                        testParent1 + "/" + testChild1NoACL + "/" + tsc1Data, "everyone",
                        "admin", true));
    }

    /* Restricted child object with own ACL, two restricted datastreams */
    @Test
    public void testWriterCanReadRestrictedChildObj()
            throws ClientProtocolException, IOException {
        assertEquals(
"Writer cannot read testparent1/testchild2WithACL!", OK
                .getStatusCode(), canRead("examplewriter",
                        testParent1 + "/" + testChild2WithACL, true));
    }

    @Test
    public void testWriterCanWriteDatastreamOnRestrictedChildObj()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot write datastream to testparent1/testchild2WithACL!",
                CREATED.getStatusCode(), canAddDS("examplewriter",
                        testParent1 + "/" + testChild2WithACL, TESTDS, true));
    }

    @Test
    public void testWriterCanAddChildToRestrictedChildObj()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot add child to testparent1/testchild2WithACL!",
                CREATED.getStatusCode(), canAddChild("examplewriter",
                        testParent1 + "/" + testChild2WithACL, TESTCHILD, true));
    }

    @Test
    public void testWriterCannotAddACLToRestrictedChildObj()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add an ACL to testparent1/testchild2WithACL!",
                FORBIDDEN.getStatusCode(), canAddACL("examplewriter",
                        testParent1 + "/" + testChild2WithACL, "everyone", "admin",
                        true));
    }

    @Test
    public void testWriterCanReadRestrictedChildObjRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot read datastream testparent1/testchild2WithACL/tsc1_data!",
                OK.getStatusCode(), canRead("examplewriter",
                        testParent1 + "/" + testChild2WithACL + "/" + tsc1Data, true));
    }

    @Test
    public void testWriterCanUpdateRestrictedChildObjRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot update datastream testparent1/testchild2WithACL/tsc1_data!",
                NO_CONTENT.getStatusCode(), canUpdateDS("examplewriter",
                        testParent1 + "/" + testChild2WithACL, tsc1Data, true));
    }

    @Test
    public void
    testWriterCannotAddACLToRestrictedChildObjRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add an ACL to datastream testparent1/testchild2WithACL/tsc1_data!",
                FORBIDDEN.getStatusCode(), canAddACL("examplewriter",
                        testParent1 + "/" + testChild2WithACL + "/" + tsc1Data, "everyone",
                        "admin", true));
    }

    /* Even more restricted datastream */
    @Test
    public void
    testWriterCanReadRestrictedChildObjReallyRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot read datastream testparent1/testchild2WithACL/tsc2_data!",
                OK.getStatusCode(), canRead("examplewriter",
                        testParent1 + "/" + testChild2WithACL + "/" + tsc2Data, true));
    }

    @Test
    public
    void
    testWriterCanUpdateRestrictedChildObjReallyRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot update datastream testparent1/testchild2WithACL/tsc2_data!",
                NO_CONTENT.getStatusCode(), canUpdateDS("examplewriter",
                        testParent1 + "/" + testChild2WithACL, tsc2Data, true));
    }

    @Test
    public
    void
    testWriterCannotAddACLToRestrictedChildObjReallyRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add an ACL to datastream testparent1/testchild2WithACL/tsc2_data!",
                FORBIDDEN.getStatusCode(), canAddACL("examplewriter",
                        testParent1 + "/" + testChild2WithACL + "/" + tsc2Data, "everyone",
                        "admin", true));
    }

    /* Writer/Admin child object with own ACL, two restricted datastreams */
    @Test
    public void testWriterCanReadWriterRestrictedChildObj()
            throws ClientProtocolException, IOException {
        assertEquals("Writer cannot read testparent1/testchild4WithACL!", OK
                .getStatusCode(), canRead("examplewriter",
                testParent1 + "/" + testChild4WithACL, true));
    }

    @Test
    public void testWriterCanWriteDatastreamOnWriterRestrictedChildObj()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot write datastream to testparent1/testchild4WithACL!",
                CREATED.getStatusCode(), canAddDS("examplewriter",
                        testParent1 + "/" + testChild4WithACL, TESTDS, true));
    }

    @Test
    public void testWriterCanAddChildToWriterRestrictedChildObj()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot add child to testparent1/testchild4WithACL!",
                CREATED.getStatusCode(), canAddChild("examplewriter",
                        testParent1 + "/" + testChild4WithACL, TESTCHILD, true));
    }

    @Test
    public void testWriterCannotAddACLToWriterRestrictedChildObj()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add an ACL to testparent1/testchild4WithACL!",
                FORBIDDEN.getStatusCode(), canAddACL("examplewriter",
                        testParent1 + "/" + testChild4WithACL, "everyone", "admin",
                        true));
    }

    @Test
    public
    void
    testWriterCanReadWriterRestrictedChildObjWriterRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot read datastream testparent1/testchild4WithACL/tsc1_data!",
                OK.getStatusCode(), canRead("examplewriter",
                        testParent1 + "/" + testChild4WithACL + "/" + tsc1Data, true));
    }

    @Test
    public
    void
    testWriterCanUpdateWriterRestrictedChildObjWriterRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot update datastream testparent1/testchild4WithACL/tsc1_data!",
                NO_CONTENT.getStatusCode(), canUpdateDS("examplewriter",
                        testParent1 + "/" + testChild4WithACL, tsc1Data, true));
    }

    @Test
    public
    void
    testWriterCannotAddACLToWriterRestrictedChildObjWriterRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add an ACL to datastream testparent1/testchild4WithACL/tsc1_data!",
                FORBIDDEN.getStatusCode(), canAddACL("examplewriter",
                        testParent1 + "/" + testChild4WithACL + "/" + tsc1Data, "everyone",
                        "admin", true));
    }

    /* Even more restricted datastream */
    @Test
    public
    void
    testWriterCannotReadWriterRestrictedChildObjReallyWriterRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to read datastream testparent1/testchild4WithACL/tsc2_data!",
                FORBIDDEN.getStatusCode(), canRead("examplewriter",
                        testParent1 + "/" + testChild4WithACL + "/" + tsc2Data, true));
    }

    @Test
    public
    void
    testWriterCannotUpdateWriterRestrictedChildObjReallyWriterRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to update datastream testparent1/testchild4WithACL/tsc2_data!",
                FORBIDDEN.getStatusCode(), canUpdateDS("examplewriter",
                        testParent1 + "/" + testChild4WithACL, tsc2Data, true));
    }

    @Test
    public
    void
    testWriterCannotAddACLToWriterRestrictedChildObjReallyWriterRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add an ACL to datastream testparent1/testchild4WithACL/tsc2_data!",
                FORBIDDEN.getStatusCode(), canAddACL("examplewriter",
                        testParent1 + "/" + testChild4WithACL + "/" + tsc2Data, "everyone",
                        "admin", true));
    }

    /* Admin object with public datastream */
    @Test
    public void testWriterCannotReadAdminObj() throws ClientProtocolException,
    IOException {
        assertEquals(
                "Writer should not be allowed to read testparent2/testchild5WithACL!",
                FORBIDDEN.getStatusCode(), canRead("examplewriter",
                        testParent2 + "/" + testChild5WithACL, true));
    }

    @Test
    public void testWriterCannotWriteDatastreamOnAdminObj()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to write datastream to testparent2/testchild5WithACL!",
                FORBIDDEN.getStatusCode(), canAddDS("examplewriter",
                        testParent2 + "/" + testChild5WithACL, TESTDS, true));
    }

    @Test
    public void testWriterCannotAddChildToAdminObj()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add child to testparent2/testchild5WithACL!",
                FORBIDDEN.getStatusCode(), canAddChild("examplewriter",
                        testParent2 + "/" + testChild5WithACL, TESTCHILD, true));
    }

    @Test
    public void testWriterCannotAddACLToAdminObj()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to to add an ACL to testparent2/testchild5WithACL!",
                FORBIDDEN.getStatusCode(), canAddACL("examplewriter",
                        testParent2 + "/" + testChild5WithACL, "everyone", "admin",
                        true));
    }

    @Test
    public void testWriterCannotReadAdminObjAdminRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to to read datastream testparent2/testchild5WithACL/tsc1_data!",
                FORBIDDEN.getStatusCode(), canRead("examplewriter",
                        testParent2 + "/" + testChild5WithACL + "/" + tsc1Data, true));
    }

    @Test
    public void testWriterCannotUpdateAdminObjAdminRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to update datastream testparent2/testchild5WithACL/tsc1_data!",
                FORBIDDEN.getStatusCode(), canUpdateDS("examplewriter",
                        testParent2 + "/" + testChild5WithACL, tsc1Data, true));
    }

    @Test
    public void testWriterCannotAddACLToAdminObjAdminRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add an ACL to datastream testparent2/testchild5WithACL/tsc1_data!",
                FORBIDDEN.getStatusCode(), canAddACL("examplewriter",
                        testParent2 + "/" + testChild5WithACL + "/" + tsc1Data, "everyone",
                        "admin", true));
    }

    @Test
    public void testWriterCanReadAdminObjPublicDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot read datastream testparent2/testchild5WithACL/tsc2_data!",
                OK.getStatusCode(), canRead("examplewriter",
                        testParent2 + "/" + tsp1Data, true));
    }

    @Test
    public void testWriterCannotUpdateAdminObjPublicDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to update datastream testparent2/testchild5WithACL/tsc2_data!",
                FORBIDDEN.getStatusCode(), canUpdateDS("examplewriter",
                        testParent2 + "/" + testChild5WithACL, tsc2Data, true));
    }

    @Test
    public void testWriterCannotAddACLToAdminObjPublicDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add an ACL to datastream testparent2/testchild5WithACL/tsc2_data!",
                FORBIDDEN.getStatusCode(), canAddACL("examplewriter",
                        testParent2 + "/" + testChild5WithACL + "/" + tsc2Data, "everyone",
                        "admin", true));
    }

    /* Deletions */
    @Test
    public void testWriterCannotDeleteOpenObj() throws ClientProtocolException,
    IOException {
        assertEquals(
                "Writer should not be allowed to delete object testparent3!",
                FORBIDDEN
                .getStatusCode(), canDelete("examplewriter", testParent3,
                        true));
    }

    @Test
    public void testWriterCanDeleteOpenObjPublicDatastream()
            throws ClientProtocolException, IOException {
        assertEquals("Writer cannot delete datastream testparent3/tsp1_data!",
                NO_CONTENT.getStatusCode(), canDelete("examplewriter",
                        testParent3 + "/" + tsp1Data, true));
    }

    @Test
    public void testWriterCannotDeleteOpenObjRestrictedDatastream()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to delete datastream testparent3/tsp2_data!",
                FORBIDDEN.getStatusCode(), canDelete("examplewriter",
                        testParent3 + "/" + tsp2Data, true));
    }

    @Test
    public void testWriterCannotDeleteRestrictedChildObj()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to delete object testparent3/testchild3a!",
                FORBIDDEN.getStatusCode(), canDelete("examplewriter",
                        testParent3 + "/" + testChild3A, true));
    }

    @Test
    public void testWriterCanDeleteInheritedACLChildObj()
            throws ClientProtocolException, IOException {
        assertEquals("Writer cannot delete object testparent3/testchild3b!",
                NO_CONTENT.getStatusCode(), canDelete("examplewriter",
                        testParent3 + "/" + testChild3B, true));
    }

    /* root node */
    @Test
    public void testWriterCannotReadRootNode()
            throws ClientProtocolException, IOException {
        assertEquals("Writer should not be allowed to read root node!",
                FORBIDDEN.getStatusCode(),
                canRead("examplewriter", "/", true));
    }

    @Test
    public void testWriterCannotWriteDatastreamOnRootNode()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to write datastream to root node!",
                FORBIDDEN
                .getStatusCode(), canAddDS("examplewriter", "/", TESTDS, true));
    }

    @Test
    public void testWriterCannotAddChildToRootNode()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add child to root node!",
                FORBIDDEN
                .getStatusCode(), canAddChild("examplewriter", "/", TESTCHILD, true));
    }

    @Test
    public void testWriterCannotAddACLToRootNode()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer should not be allowed to add an ACL to root node!",
                FORBIDDEN
                .getStatusCode(), canAddACL("examplewriter", "/", "everyone",
                        "admin", true));
    }

    @Test
    public void testWriterReaderCanReadACL() throws ClientProtocolException, IOException {
        assertEquals("Writer-reader should be allowed to read ACL permissions", OK.getStatusCode(), canGetRoles(
                "exampleWriterReader", testParent4, true));
    }

    @Ignore("Awaiting bug fix for story 72982948")
    @Test
    public void testWriterCanAddChildToRestrictedChildObjUnderRestrictedParent()
            throws ClientProtocolException, IOException {
        assertEquals(
                "Writer cannot add child to testparent4/testchild4WithACL!",
                CREATED.getStatusCode(), canAddChild("examplewriter",
                        testParent4 + "/" + testChild4WithACL, TESTCHILD, true));
    }
}
