/**
 * Copyright (C) 2014 OpenTravel Alliance (info@opentravel.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opentravel.schemacompiler.console;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opentravel.ns.ota2.security_v01_00.RepositoryPermission;
import org.opentravel.schemacompiler.model.TLLibraryStatus;
import org.opentravel.schemacompiler.repository.RepositoryItem;
import org.opentravel.schemacompiler.security.RepositorySecurityManager;
import org.opentravel.schemacompiler.security.UserPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller that handles interactions with the view item page(s) of the OTA2.0 repository console.
 * 
 * @author S. Livezey
 */
@Controller
public class ViewItemController extends BaseController {

    private static Log log = LogFactory.getLog(BrowseController.class);

    /**
     * Called by the Spring MVC controller to display the application browse page.
     * 
     * @param rootNamespace
     *            the root namespace of the selected library
     * @param path
     *            the sub-namespace path relative to the base namespace
     * @param filename
     *            the filename of the selected library to view
     * @param version
     *            the version of the selected library to view
     * @param session
     *            the HTTP session that contains information about an authenticated user
     * @param model
     *            the model context to be used when rendering the page view
     * @return String
     */
    @RequestMapping({ "/itemDetails.html", "/itemDetails.htm" })
    public String itemDetails(@RequestParam(value = "baseNamespace") String baseNamespace,
            @RequestParam(value = "filename") String filename,
            @RequestParam(value = "version") String version, HttpSession session, Model model) {
        String targetPage = null;
        try {
            RepositorySecurityManager securityManager = getSecurityManager();
            UserPrincipal user = getCurrentUser(session);
            RepositoryItem item = getRepositoryManager().getRepositoryItem(baseNamespace, filename,
                    version);
            RepositoryPermission requiredPermission = (item.getStatus() == TLLibraryStatus.DRAFT) ? RepositoryPermission.READ_DRAFT
                    : RepositoryPermission.READ_FINAL;

            if (securityManager.isAuthorized(user, item.getNamespace(), requiredPermission)) {
                model.addAttribute("item", item);

            } else {
                setErrorMessage("You are not authorized to view the requested repository item.",
                        model);
                targetPage = new SearchController().searchPage(null, false, false, session, model);
            }

        } catch (Throwable t) {
            log.error("An error occured while displaying the repository item.", t);
            setErrorMessage(
                    "An error occured while displaying the repository item (see server log for details).",
                    model);
        }

        if (targetPage == null) {
            targetPage = applyCommonValues(model, "itemDetails");
        }
        return targetPage;
    }

}
