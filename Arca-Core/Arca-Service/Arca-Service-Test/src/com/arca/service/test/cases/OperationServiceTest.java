/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
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
package com.arca.service.test.cases;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;

import com.arca.service.OperationService;

public class OperationServiceTest extends ServiceTestCase<OperationService> {

	public OperationServiceTest() {
		super(OperationService.class);
	}

	public void testOperationServiceActionStart() {
		assertEquals(OperationService.Action.START, OperationService.Action.valueOf("START"));
	}

	public void testOperationServiceActionCancel() {
		assertEquals(OperationService.Action.CANCEL, OperationService.Action.valueOf("CANCEL"));
	}

	public void testOperationServiceActionValues() {
		final OperationService.Action[] actions = OperationService.Action.values();
		assertEquals(OperationService.Action.START, actions[0]);
		assertEquals(OperationService.Action.CANCEL, actions[1]);
	}

	// ===========================================

	public void testOperationServiceStartable() {
		final Intent intent = new Intent(getContext(), OperationService.class);
		startService(intent);
	}

	public void testOperationServiceNotBindable() {
		final Intent intent = new Intent(getContext(), OperationService.class);
		final IBinder binder = bindService(intent);
		assertNull(binder);
	}

	// ===========================================

	public void testOperationServiceStartWithoutContext() {
		assertFalse(OperationService.start(null, null));
	}

	public void testOperationServiceCancelWithoutContext() {
		assertFalse(OperationService.cancel(null, null));
	}

	public void testOperationServiceStartWithoutOperation() {
		assertFalse(OperationService.start(getContext(), null));
	}

	public void testOperationServiceCancelWithoutOperation() {
		assertFalse(OperationService.cancel(getContext(), null));
	}

}
