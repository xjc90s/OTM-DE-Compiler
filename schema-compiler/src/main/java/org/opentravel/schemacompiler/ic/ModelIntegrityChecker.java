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

package org.opentravel.schemacompiler.ic;

import org.opentravel.schemacompiler.event.ModelEventListener;
import org.opentravel.schemacompiler.model.TLModelElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Default model integrity checker instance that provides a standard set of listeners. If required, this class may be
 * extended to include additional routines required for application-specific business rules.
 * 
 * @author S. Livezey
 */
public class ModelIntegrityChecker extends AbstractModelIntegrityChecker {

    /**
     * @see org.opentravel.schemacompiler.ic.AbstractModelIntegrityChecker#getListeners()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected List<ModelEventListener<?,?>> getListeners() {
        List<ModelEventListener<?,?>> listeners = new ArrayList<>();

        listeners.add( new TypeAssignmentChangeIntegrityChecker() );
        listeners.add( new LibraryMemberChangeIntegrityChecker() );
        listeners.add( new FacetMemberChangeIntegrityChecker() );
        listeners.add( new ResourceMemberChangeIntegrityChecker() );
        listeners.add( new TypeNameIntegrityChecker() );

        listeners.add( new NameChangeIntegrityChecker() );
        listeners.add( new PrefixChangeIntegrityChecker() );
        listeners.add( new NamespaceChangeIntegrityChecker() );

        listeners.add( new LibraryRemovedIntegrityChecker() );
        listeners.add( new ModelElementRemovedIntegrityChecker<TLModelElement>( TLModelElement.class ) );

        listeners.add( new ContextDeletionIntegrityChecker() );
        listeners.add( new ContextDeclarationChangeIntegrityChecker() );
        listeners.add( new ContextReferrerAddedIntegrityChecker() );
        listeners.add( new ContextLibraryMemberAddedIntegrityChecker() );
        listeners.add( new ContextReferrerChangeIntegrityChecker() );

        return listeners;
    }

}
