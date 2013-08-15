package com.xtreme.rest.service.test.android;

import android.content.Context;

import com.xtreme.rest.service.RestService;

@SuppressWarnings("unused")
public final class Tests {

	public static final String TAG = "RESTEST";
	
	private Context mContext;

	public Tests(Context context) {
		mContext = context;
	}
	
	public void run() {
		test6.run();
	}
	
	private final Runnable test0 = new Runnable() {
		@Override
		public void run() {
			RestService.start(mContext, new MultipleTaskOperation(1, new int[] {1}));
		}
	};
	
	private final Runnable test1 = new Runnable() {
		@Override
		public void run() {
			RestService.start(mContext, new MultipleTaskOperation(1, new int[] {1, 2, 3}));
		}
		
		/**
		 * 04-25 11:23:56.837: onCreateTasks for Operation: O1
		 * 04-25 11:23:56.847: onTaskStarted for operation: O1, T1
		 * 04-25 11:23:56.847: onExecuteNetworkRequest for task: T1
		 * 04-25 11:23:56.847: onTaskStarted for operation: O1, T2
		 * 04-25 11:23:56.857: onTaskStarted for operation: O1, T3
		 * 04-25 11:23:56.867: onExecuteNetworkRequest for task: T2
		 * 
		 * 04-25 11:23:57.868: onExecuteNetworkRequest for task: T3
		 * 04-25 11:23:57.868: onExecuteProcessingRequest for task: T1
		 * 
		 * 04-25 11:23:58.869: onTaskComplete for operation: O1, T1
		 * 04-25 11:23:58.899: onExecuteProcessingRequest for task: T2
		 * 
		 * 04-25 11:23:59.910: onTaskComplete for operation: O1, T2
		 * 04-25 11:23:59.920: onExecuteProcessingRequest for task: T3
		 * 
		 * 04-25 11:24:00.921: onTaskComplete for operation: O1, T3
		 * 04-25 11:24:00.921: onComplete for operation: O1
		 * 04-25 11:24:00.931: Success for operation: O1
		 */
	};
	
	private final Runnable test2 = new Runnable() {
		@Override
		public void run() {
			RestService.start(mContext, new MultipleTaskOperation(1, new int[] {1, 2, 3}));
			RestService.start(mContext, new MultipleTaskOperation(2, new int[] {1, 2, 3}));
		}
		
		/**
		 * 04-25 11:12:44.187: onCreateTasks for Operation: O1
		 * 04-25 11:12:44.197: onTaskStarted for operation: O1, T1
		 * 04-25 11:12:44.207: onExecuteNetworkRequest for task: T1
		 * 04-25 11:12:44.207: onTaskStarted for operation: O1, T2
		 * 04-25 11:12:44.207: onTaskStarted for operation: O1, T3
		 * 04-25 11:12:44.227: onExecuteNetworkRequest for task: T2
		 * 04-25 11:12:44.227: onCreateTasks for Operation: O2
		 * 04-25 11:12:44.237: onTaskStarted for operation: O2, T1
		 * 04-25 11:12:44.247: onTaskStarted for operation: O2, T2
		 * 04-25 11:12:44.257: onTaskStarted for operation: O2, T3
		 * 
		 * 04-25 11:12:45.278: onExecuteProcessingRequest for task: T1
		 * 04-25 11:12:45.309: onExecuteNetworkRequest for task: T3
		 * 
		 * 04-25 11:12:46.290: onTaskComplete for operation: O2, T1
		 * 04-25 11:12:46.290: onTaskComplete for operation: O1, T1
		 * 04-25 11:12:46.290: onExecuteProcessingRequest for task: T2
		 * 
		 * 04-25 11:12:47.301: onTaskComplete for operation: O2, T2
		 * 04-25 11:12:47.311: onTaskComplete for operation: O1, T2
		 * 04-25 11:12:47.321: onExecuteProcessingRequest for task: T3
		 * 
		 * 04-25 11:12:48.332: onTaskComplete for operation: O1, T3
		 * 04-25 11:12:48.332: onComplete for operation: O1
		 * 04-25 11:12:48.332: Success for operation: O1
		 * 04-25 11:12:48.342: onTaskComplete for operation: O2, T3
		 * 04-25 11:12:48.342: onComplete for operation: O2
		 * 04-25 11:12:48.352: Success for operation: O2
		 */
	};
	
	private final Runnable test3 = new Runnable() {
		@Override
		public void run() {
			RestService.start(mContext, new MultipleTaskOperation(1, new int[] {1, 2, 3}));
			RestService.start(mContext, new MultipleTaskOperation(2, new int[] {1, 2, 4}));
		}
		
		/**
		 * 04-25 11:49:25.017: onCreateTasks for Operation: O1
		 * 04-25 11:49:25.027: onTaskStarted for operation: O1, T1
		 * 04-25 11:49:25.037: onTaskStarted for operation: O1, T2
		 * 04-25 11:49:25.037: onExecuteNetworkRequest for task: T2
		 * 04-25 11:49:25.047: onTaskStarted for operation: O1, T3
		 * 04-25 11:49:25.047: onExecuteNetworkRequest for task: T1
		 * 04-25 11:49:25.057: onCreateTasks for Operation: O2
		 * 04-25 11:49:25.057: onTaskStarted for operation: O2, T1
		 * 04-25 11:49:25.077: onTaskStarted for operation: O2, T2
		 * 04-25 11:49:25.087: onTaskStarted for operation: O2, T4
		 * 
		 * 04-25 11:49:26.058: onExecuteNetworkRequest for task: T4
		 * 04-25 11:49:26.068: onExecuteProcessingRequest for task: T2
		 * 04-25 11:49:26.068: onExecuteNetworkRequest for task: T3
		 * 
		 * 04-25 11:49:27.079: onTaskComplete for operation: O1, T2
		 * 04-25 11:49:27.079: onTaskComplete for operation: O2, T2
		 * 04-25 11:49:27.109: onExecuteProcessingRequest for task: T1
		 * 
		 * 04-25 11:49:28.130: onTaskComplete for operation: O2, T1
		 * 04-25 11:49:28.140: onTaskComplete for operation: O1, T1
		 * 04-25 11:49:28.150: onExecuteProcessingRequest for task: T4
		 * 
		 * 04-25 11:49:29.161: onTaskComplete for operation: O2, T4
		 * 04-25 11:49:29.181: onComplete for operation: O2
		 * 04-25 11:49:29.191: Success for operation: O2
		 * 04-25 11:49:29.221: onExecuteProcessingRequest for task: T3
		 * 
		 * 04-25 11:49:30.232: onTaskComplete for operation: O1, T3
		 * 04-25 11:49:30.232: onComplete for operation: O1
		 * 04-25 11:49:30.232: Success for operation: O1
		 */
	};
	
	private final Runnable test4 = new Runnable() {
		@Override
		public void run() {
			RestService.start(mContext, new LinearDependencyOperation(1, new int[] {1, 2, 3}));
		}
		
		/**
		 * 04-25 14:50:02.170: onCreateTasks for Operation: O1
		 * 04-25 14:50:02.180: onTaskStarted for operation: O1, T1
		 * 04-25 14:50:02.190: onExecuteNetworkRequest for task: T1
		 * 
		 * 04-25 14:50:03.231: onExecuteProcessingRequest for task: T1
		 * 
		 * 04-25 14:50:04.242: onTaskComplete for operation: O1, T1
		 * 04-25 14:50:04.272: onTaskStarted for operation: O1, T2
		 * 04-25 14:50:04.282: onExecuteNetworkRequest for task: T2
		 * 
		 * 04-25 14:50:05.303: onExecuteProcessingRequest for task: T2
		 * 
		 * 04-25 14:50:06.304: onTaskComplete for operation: O1, T2
		 * 04-25 14:50:06.345: onExecuteNetworkRequest for task: T3
		 * 04-25 14:50:06.355: onTaskStarted for operation: O1, T3
		 * 
		 * 04-25 14:50:07.366: onExecuteProcessingRequest for task: T3
		 * 
		 * 04-25 14:50:08.377: onTaskComplete for operation: O1, T3
		 * 04-25 14:50:08.377: onComplete for operation: O1
		 * 04-25 14:50:08.387: Success for operation: O1
		 */
	};
	
	private final Runnable test5 = new Runnable() {
		@Override
		public void run() {
			RestService.start(mContext, new LinearDependencyOperation(1, new int[] {1, 2, 3}));
			RestService.start(mContext, new MultipleTaskOperation(2, new int[] {1, 2, 3}));
		}

		/**
		 * 04-25 19:30:32.059: onCreateTasks for Operation: O1
		 * 04-25 19:30:32.064: onExecuteNetworkRequest for task: T1
		 * 04-25 19:30:32.069: onTaskStarted for operation: O1, T1
		 * 04-25 19:30:32.069: onCreateTasks for Operation: O2
		 * 04-25 19:30:32.069: onTaskStarted for operation: O2, T1
		 * 04-25 19:30:32.074: onExecuteNetworkRequest for task: T2
		 * 04-25 19:30:32.074: onTaskStarted for operation: O2, T2
		 * 04-25 19:30:32.079: onTaskStarted for operation: O2, T3
		 * 
		 * 04-25 19:30:33.109: onExecuteProcessingRequest for task: T1
		 * 04-25 19:30:33.134: onExecuteNetworkRequest for task: T3
		 * 
		 * 04-25 19:30:34.119: onTaskComplete for operation: O1, T1
		 * 04-25 19:30:34.149: onExecuteNetworkRequest for task: T2
		 * 04-25 19:30:34.149: onTaskStarted for operation: O1, T2
		 * 04-25 19:30:34.149: onTaskComplete for operation: O2, T1
		 * 04-25 19:30:34.154: onExecuteProcessingRequest for task: T3
		 * 
		 * 04-25 19:30:35.169: onTaskComplete for operation: O2, T3
		 * 04-25 19:30:35.174: onExecuteProcessingRequest for task: T2
		 * 
		 * 04-25 19:30:36.184: onTaskComplete for operation: O2, T2
		 * 04-25 19:30:36.184: onComplete for operation: O2
		 * 04-25 19:30:36.189: Success for operation: O2
		 * 04-25 19:30:36.194: onTaskComplete for operation: O1, T2
		 * 04-25 19:30:36.199: onExecuteNetworkRequest for task: T3
		 * 04-25 19:30:36.204: onTaskStarted for operation: O1, T3
		 * 
		 * 04-25 19:30:37.219: onExecuteProcessingRequest for task: T3
		 * 
		 * 04-25 19:30:38.224: onTaskComplete for operation: O1, T3
		 * 04-25 19:30:38.224: onComplete for operation: O1
		 * 04-25 19:30:38.224: Success for operation: O1
		 */
	};
	
	private final Runnable test6 = new Runnable() {
		@Override
		public void run() {
			RestService.start(mContext, new DiamondGraphOperation(1, 1, new int[] {2, 3, 4}, 5));
		}
		
		/**
		 * 04-25 20:05:21.369: onCreateTasks for Operation: O1
		 * 04-25 20:05:21.389: onTaskStarted for operation: O1, T1
		 * 04-25 20:05:21.394: onExecuteNetworkRequest for task: T1
		 * 
		 * 04-25 20:05:22.409: onExecuteProcessingRequest for task: T1
		 * 
		 * 04-25 20:05:23.424: onTaskComplete for operation: O1, T1
		 * 04-25 20:05:23.444: onTaskStarted for operation: O1, T4
		 * 04-25 20:05:23.449: onExecuteNetworkRequest for task: T4
		 * 04-25 20:05:23.454: onTaskStarted for operation: O1, T2
		 * 04-25 20:05:23.454: onExecuteNetworkRequest for task: T2
		 * 04-25 20:05:23.464: onTaskStarted for operation: O1, T3
		 * 
		 * 04-25 20:05:24.474: onExecuteNetworkRequest for task: T3
		 * 04-25 20:05:24.479: onExecuteProcessingRequest for task: T2
		 * 
		 * 04-25 20:05:25.494: onTaskComplete for operation: O1, T2
		 * 04-25 20:05:25.504: onExecuteProcessingRequest for task: T3
		 * 
		 * 04-25 20:05:26.509: onTaskComplete for operation: O1, T3
		 * 04-25 20:05:26.519: onExecuteProcessingRequest for task: T4
		 * 
		 * 04-25 20:05:27.524: onTaskComplete for operation: O1, T4
		 * 04-25 20:05:27.539: onExecuteNetworkRequest for task: T5
		 * 04-25 20:05:27.544: onTaskStarted for operation: O1, T5
		 * 
		 * 04-25 20:05:28.559: onExecuteProcessingRequest for task: T5
		 * 
		 * 04-25 20:05:29.564: onTaskComplete for operation: O1, T5
		 * 04-25 20:05:29.569: onComplete for operation: O1
		 * 04-25 20:05:29.569: Success for operation: O1
		 */

	};
	
	private final Runnable test7 = new Runnable() {
		@Override
		public void run() {
			RestService.start(mContext, new ReallyComplexOperation(1));
		}
		
		/**
		 * 04-25 20:45:12.810: onCreateTasks for Operation: O1
		 * 04-25 20:45:12.855: onTaskStarted for operation: O1, T0
		 * 04-25 20:45:12.860: onExecuteNetworkRequest for task: T0
		 * 04-25 20:45:12.865: onTaskStarted for operation: O1, T1
		 * 04-25 20:45:12.865: onExecuteNetworkRequest for task: T1
		 * 04-25 20:45:12.865: onTaskStarted for operation: O1, T2
		 * 
		 * 04-25 20:45:13.880: onExecuteProcessingRequest for task: T0
		 * 04-25 20:45:13.885: onExecuteNetworkRequest for task: T2
		 * 
		 * 04-25 20:45:14.885: onTaskComplete for operation: O1, T0
		 * 04-25 20:45:14.910: onExecuteProcessingRequest for task: T1
		 * 
		 * 04-25 20:45:15.915: onTaskComplete for operation: O1, T1
		 * 04-25 20:45:15.920: onTaskStarted for operation: O1, T4
		 * 04-25 20:45:15.925: onExecuteNetworkRequest for task: T4
		 * 04-25 20:45:15.925: onTaskStarted for operation: O1, T3
		 * 04-25 20:45:15.930: onExecuteNetworkRequest for task: T3
		 * 04-25 20:45:15.930: onExecuteProcessingRequest for task: T2
		 * 
		 * 04-25 20:45:16.940: onTaskComplete for operation: O1, T2
		 * 04-25 20:45:16.955: onExecuteProcessingRequest for task: T3
		 * 
		 * 04-25 20:45:17.970: onTaskComplete for operation: O1, T3
		 * 04-25 20:45:17.985: onTaskStarted for operation: O1, T5
		 * 04-25 20:45:17.995: onExecuteNetworkRequest for task: T5
		 * 
		 * 04-25 20:45:18.000: onExecuteProcessingRequest for task: T4
		 * 
		 * 04-25 20:45:19.005: onTaskComplete for operation: O1, T4
		 * 04-25 20:45:19.020: onExecuteProcessingRequest for task: T5
		 * 
		 * 04-25 20:45:20.025: onTaskComplete for operation: O1, T5
		 * 04-25 20:45:20.050: onTaskStarted for operation: O1, T6
		 * 04-25 20:45:20.055: onExecuteNetworkRequest for task: T6
		 * 
		 * 04-25 20:45:21.070: onExecuteProcessingRequest for task: T6
		 * 
		 * 04-25 20:45:22.080: onTaskComplete for operation: O1, T6
		 * 04-25 20:45:22.090: onTaskStarted for operation: O1, T9
		 * 04-25 20:45:22.095: onExecuteNetworkRequest for task: T9
		 * 04-25 20:45:22.105: onTaskStarted for operation: O1, T8
		 * 04-25 20:45:22.110: onExecuteNetworkRequest for task: T8
		 * 04-25 20:45:22.110: onTaskStarted for operation: O1, T7
		 * 
		 * 04-25 20:45:23.110: onExecuteProcessingRequest for task: T9
		 * 04-25 20:45:23.110: onExecuteNetworkRequest for task: T7
		 * 
		 * 04-25 20:45:24.125: onTaskComplete for operation: O1, T9
		 * 04-25 20:45:24.130: onExecuteProcessingRequest for task: T7
		 * 
		 * 04-25 20:45:25.135: onTaskComplete for operation: O1, T7
		 * 04-25 20:45:25.140: onExecuteProcessingRequest for task: T8
		 * 
		 * 04-25 20:45:26.145: onTaskComplete for operation: O1, T8
		 * 04-25 20:45:26.175: onTaskStarted for operation: O1, T10
		 * 04-25 20:45:26.185: onExecuteNetworkRequest for task: T10
		 * 
		 * 04-25 20:45:27.200: onExecuteProcessingRequest for task: T10
		 * 
		 * 04-25 20:45:28.205: onTaskComplete for operation: O1, T10
		 * 04-25 20:45:28.220: onExecuteNetworkRequest for task: T13
		 * 04-25 20:45:28.220: onTaskStarted for operation: O1, T13
		 * 04-25 20:45:28.235: onTaskStarted for operation: O1, T12
		 * 04-25 20:45:28.240: onExecuteNetworkRequest for task: T12
		 * 04-25 20:45:28.250: onTaskStarted for operation: O1, T11
		 * 
		 * 04-25 20:45:29.240: onExecuteProcessingRequest for task: T13
		 * 04-25 20:45:29.240: onExecuteNetworkRequest for task: T11
		 * 
		 * 04-25 20:45:30.245: onTaskComplete for operation: O1, T13
		 * 04-25 20:45:30.255: onExecuteProcessingRequest for task: T12
		 * 
		 * 04-25 20:45:31.260: onTaskComplete for operation: O1, T12
		 * 04-25 20:45:31.265: onExecuteProcessingRequest for task: T11
		 * 
		 * 04-25 20:45:32.270: onTaskComplete for operation: O1, T11
		 * 04-25 20:45:32.305: onExecuteNetworkRequest for task: T14
		 * 04-25 20:45:32.310: onTaskStarted for operation: O1, T14
		 * 
		 * 04-25 20:45:33.325: onExecuteProcessingRequest for task: T14
		 * 
		 * 04-25 20:45:34.330: onTaskComplete for operation: O1, T14
		 * 04-25 20:45:34.330: onComplete for operation: O1
		 * 04-25 20:45:34.335: Success for operation: O1
		 */
	};
	
	private final Runnable test8 = new Runnable() {
		@Override
		public void run() {
			RestService.start(mContext, new ReallyComplexOperation(1));
			RestService.start(mContext, new ReallyComplexOperation(2));
		}
		
		/**
		 * 04-25 21:10:21.790: onCreateTasks for Operation: O1
		 * 04-25 21:10:21.790: onTaskStarted for operation: O1, T0
		 * 04-25 21:10:21.790: onExecuteNetworkRequest for task: T0
		 * 04-25 21:10:21.790: onTaskStarted for operation: O1, T1
		 * 04-25 21:10:21.790: onExecuteNetworkRequest for task: T1
		 * 04-25 21:10:21.790: onTaskStarted for operation: O1, T2
		 * 04-25 21:10:21.815: onCreateTasks for Operation: O2
		 * 04-25 21:10:21.815: onTaskStarted for operation: O2, T0
		 * 04-25 21:10:21.815: onTaskStarted for operation: O2, T1
		 * 04-25 21:10:21.815: onTaskStarted for operation: O2, T2
		 * 
		 * 04-25 21:10:22.795: onExecuteNetworkRequest for task: T2
		 * 04-25 21:10:22.800: onExecuteProcessingRequest for task: T0
		 * 
		 * 04-25 21:10:23.800: onTaskComplete for operation: O1, T0
		 * 04-25 21:10:23.800: onTaskComplete for operation: O2, T0
		 * 04-25 21:10:23.805: onExecuteProcessingRequest for task: T2
		 * 
		 * 04-25 21:10:24.810: onTaskComplete for operation: O1, T2
		 * 04-25 21:10:24.810: onTaskComplete for operation: O2, T2
		 * 04-25 21:10:24.810: onExecuteProcessingRequest for task: T1
		 * 
		 * 04-25 21:10:25.810: onTaskComplete for operation: O1, T1
		 * 04-25 21:10:25.815: onExecuteNetworkRequest for task: T3
		 * 04-25 21:10:25.815: onTaskStarted for operation: O1, T3
		 * 04-25 21:10:25.815: onTaskStarted for operation: O1, T4
		 * 04-25 21:10:25.815: onTaskComplete for operation: O2, T1
		 * 04-25 21:10:25.815: onExecuteNetworkRequest for task: T4
		 * 04-25 21:10:25.815: onTaskStarted for operation: O2, T3
		 * 04-25 21:10:25.815: onTaskStarted for operation: O2, T4
		 * 
		 * 04-25 21:10:26.840: onExecuteProcessingRequest for task: T3
		 * 
		 * 04-25 21:10:27.845: onTaskComplete for operation: O2, T3
		 * 04-25 21:10:27.845: onTaskStarted for operation: O2, T5
		 * 04-25 21:10:27.845: onTaskComplete for operation: O1, T3
		 * 04-25 21:10:27.845: onExecuteNetworkRequest for task: T5
		 * 04-25 21:10:27.850: onTaskStarted for operation: O1, T5
		 * 04-25 21:10:27.850: onExecuteProcessingRequest for task: T4
		 * 
		 * 04-25 21:10:28.855: onTaskComplete for operation: O2, T4
		 * 04-25 21:10:28.855: onTaskComplete for operation: O1, T4
		 * 04-25 21:10:28.860: onExecuteProcessingRequest for task: T5
		 * 
		 * 04-25 21:10:29.860: onTaskComplete for operation: O2, T5
		 * 04-25 21:10:29.860: onExecuteNetworkRequest for task: T6
		 * 04-25 21:10:29.865: onTaskStarted for operation: O2, T6
		 * 04-25 21:10:29.865: onTaskComplete for operation: O1, T5
		 * 04-25 21:10:29.865: onTaskStarted for operation: O1, T6
		 * 
		 * 04-25 21:10:30.870: onExecuteProcessingRequest for task: T6
		 * 
		 * 04-25 21:10:31.875: onTaskComplete for operation: O1, T6
		 * 04-25 21:10:31.875: onTaskStarted for operation: O1, T7
		 * 04-25 21:10:31.875: onExecuteNetworkRequest for task: T7
		 * 04-25 21:10:31.880: onTaskStarted for operation: O1, T9
		 * 04-25 21:10:31.880: onExecuteNetworkRequest for task: T9
		 * 04-25 21:10:31.880: onTaskStarted for operation: O1, T8
		 * 04-25 21:10:31.880: onTaskComplete for operation: O2, T6
		 * 04-25 21:10:31.885: onTaskStarted for operation: O2, T8
		 * 04-25 21:10:31.885: onTaskStarted for operation: O2, T9
		 * 04-25 21:10:31.885: onTaskStarted for operation: O2, T7
		 * 
		 * 04-25 21:10:32.885: onExecuteNetworkRequest for task: T8
		 * 04-25 21:10:32.885: onExecuteProcessingRequest for task: T7
		 * 
		 * 04-25 21:10:33.890: onTaskComplete for operation: O2, T7
		 * 04-25 21:10:33.890: onTaskComplete for operation: O1, T7
		 * 04-25 21:10:33.890: onExecuteProcessingRequest for task: T9
		 * 
		 * 04-25 21:10:34.895: onTaskComplete for operation: O1, T9
		 * 04-25 21:10:34.895: onTaskComplete for operation: O2, T9
		 * 04-25 21:10:34.895: onExecuteProcessingRequest for task: T8
		 * 
		 * 04-25 21:10:35.900: onTaskComplete for operation: O1, T8
		 * 04-25 21:10:35.900: onExecuteNetworkRequest for task: T10
		 * 04-25 21:10:35.905: onTaskStarted for operation: O1, T10
		 * 04-25 21:10:35.905: onTaskComplete for operation: O2, T8
		 * 04-25 21:10:35.910: onTaskStarted for operation: O2, T10
		 * 
		 * 04-25 21:10:36.910: onExecuteProcessingRequest for task: T10
		 * 
		 * 04-25 21:10:37.910: onTaskComplete for operation: O2, T10
		 * 04-25 21:10:37.915: onExecuteNetworkRequest for task: T11
		 * 04-25 21:10:37.915: onTaskStarted for operation: O2, T11
		 * 04-25 21:10:37.915: onTaskStarted for operation: O2, T13
		 * 04-25 21:10:37.920: onTaskStarted for operation: O2, T12
		 * 04-25 21:10:37.920: onTaskComplete for operation: O1, T10
		 * 04-25 21:10:37.920: onExecuteNetworkRequest for task: T13
		 * 04-25 21:10:37.920: onTaskStarted for operation: O1, T12
		 * 04-25 21:10:37.920: onTaskStarted for operation: O1, T11
		 * 04-25 21:10:37.925: onTaskStarted for operation: O1, T13
		 * 
		 * 04-25 21:10:38.925: onExecuteNetworkRequest for task: T12
		 * 04-25 21:10:38.925: onExecuteProcessingRequest for task: T11
		 * 04-25 21:10:39.930: onTaskComplete for operation: O1, T11
		 * 04-25 21:10:39.930: onTaskComplete for operation: O2, T11
		 * 04-25 21:10:39.940: onExecuteProcessingRequest for task: T12
		 * 
		 * 04-25 21:10:40.940: onTaskComplete for operation: O2, T12
		 * 04-25 21:10:40.940: onTaskComplete for operation: O1, T12
		 * 04-25 21:10:40.950: onExecuteProcessingRequest for task: T13
		 * 
		 * 04-25 21:10:41.955: onTaskComplete for operation: O2, T13
		 * 04-25 21:10:41.960: onTaskStarted for operation: O2, T14
		 * 04-25 21:10:41.960: onExecuteNetworkRequest for task: T14
		 * 04-25 21:10:41.965: onTaskComplete for operation: O1, T13
		 * 04-25 21:10:41.965: onTaskStarted for operation: O1, T14
		 * 
		 * 04-25 21:10:42.970: onExecuteProcessingRequest for task: T14
		 * 04-25 21:10:43.970: onTaskComplete for operation: O1, T14
		 * 04-25 21:10:43.970: onComplete for operation: O1
		 * 04-25 21:10:43.970: Success for operation: O1
		 * 04-25 21:10:43.975: onTaskComplete for operation: O2, T14
		 * 04-25 21:10:43.980: onComplete for operation: O2
		 * 04-25 21:10:43.980: Success for operation: O2
		 */
	};
	
	private final Runnable test9 = new Runnable() {
		@Override
		public void run() {
			RestService.start(mContext, new MultipleTaskOperation(0, new int[] {1, 2, 3}));
			RestService.start(mContext, new DiamondGraphOperation(1, 2, new int[] {11, 12, 5}, 7));
			RestService.start(mContext, new LinearDependencyOperation(2, new int[] {1, 7, 2}));
			RestService.start(mContext, new DiamondGraphOperation(3, 3, new int[] {14, 2, 5}, 14));
			RestService.start(mContext, new ReallyComplexOperation(4));
		}
		
		/**
		 * All 5 operations succeeded, I did not analyze the log line-by-line, but it is attached just in case.
		 * 
		 * 04-25 21:21:08.630: onCreateTasks for Operation: O0
		 * 04-25 21:21:08.630: onExecuteNetworkRequest for task: T1
		 * 04-25 21:21:08.630: onTaskStarted for operation: O0, T1
		 * 04-25 21:21:08.630: onExecuteNetworkRequest for task: T2
		 * 04-25 21:21:08.630: onTaskStarted for operation: O0, T2
		 * 04-25 21:21:08.630: onTaskStarted for operation: O0, T3
		 * 04-25 21:21:08.645: onCreateTasks for Operation: O1
		 * 04-25 21:21:08.645: onTaskStarted for operation: O1, T2
		 * 04-25 21:21:08.645: onCreateTasks for Operation: O2
		 * 04-25 21:21:08.645: onTaskStarted for operation: O2, T1
		 * 04-25 21:21:08.650: onCreateTasks for Operation: O3
		 * 04-25 21:21:08.650: onTaskStarted for operation: O3, T3
		 * 04-25 21:21:08.650: onCreateTasks for Operation: O4
		 * 04-25 21:21:08.650: onTaskStarted for operation: O4, T0
		 * 04-25 21:21:08.650: onTaskStarted for operation: O4, T1
		 * 04-25 21:21:08.650: onTaskStarted for operation: O4, T2
		 * 04-25 21:21:09.640: onExecuteProcessingRequest for task: T1
		 * 04-25 21:21:09.640: onExecuteNetworkRequest for task: T0
		 * 04-25 21:21:09.645: onExecuteNetworkRequest for task: T3
		 * 04-25 21:21:10.645: onTaskComplete for operation: O4, T1
		 * 04-25 21:21:10.650: onTaskComplete for operation: O2, T1
		 * 04-25 21:21:10.655: onExecuteNetworkRequest for task: T7
		 * 04-25 21:21:10.655: onTaskStarted for operation: O2, T7
		 * 04-25 21:21:10.655: onTaskComplete for operation: O0, T1
		 * 04-25 21:21:10.660: onExecuteProcessingRequest for task: T3
		 * 04-25 21:21:11.665: onTaskComplete for operation: O0, T3
		 * 04-25 21:21:11.665: onTaskComplete for operation: O3, T3
		 * 04-25 21:21:11.665: onExecuteNetworkRequest for task: T2
		 * 04-25 21:21:11.665: onTaskStarted for operation: O3, T2
		 * 04-25 21:21:11.665: onExecuteNetworkRequest for task: T14
		 * 04-25 21:21:11.665: onTaskStarted for operation: O3, T14
		 * 04-25 21:21:11.670: onTaskStarted for operation: O3, T5
		 * 04-25 21:21:11.675: onExecuteProcessingRequest for task: T7
		 * 04-25 21:21:12.665: onExecuteNetworkRequest for task: T5
		 * 04-25 21:21:12.675: onTaskComplete for operation: O2, T7
		 * 04-25 21:21:12.675: onTaskStarted for operation: O2, T2
		 * 04-25 21:21:12.680: onExecuteNetworkRequest for task: T2
		 * 04-25 21:21:12.685: onExecuteProcessingRequest for task: T14
		 * 04-25 21:21:13.690: onTaskComplete for operation: O3, T14
		 * 04-25 21:21:13.695: onExecuteProcessingRequest for task: T2
		 * 04-25 21:21:14.700: onTaskComplete for operation: O2, T2
		 * 04-25 21:21:14.700: onComplete for operation: O2
		 * 04-25 21:21:14.700: Success for operation: O2
		 * 04-25 21:21:14.705: onTaskComplete for operation: O3, T2
		 * 04-25 21:21:14.710: onTaskComplete for operation: O1, T2
		 * 04-25 21:21:14.715: onExecuteNetworkRequest for task: T11
		 * 04-25 21:21:14.715: onTaskStarted for operation: O1, T11
		 * 04-25 21:21:14.720: onTaskStarted for operation: O1, T5
		 * 04-25 21:21:14.720: onExecuteNetworkRequest for task: T5
		 * 04-25 21:21:14.725: onTaskStarted for operation: O1, T12
		 * 04-25 21:21:14.725: onTaskComplete for operation: O4, T2
		 * 04-25 21:21:14.725: onTaskComplete for operation: O0, T2
		 * 04-25 21:21:14.725: onComplete for operation: O0
		 * 04-25 21:21:14.730: Success for operation: O0
		 * 04-25 21:21:14.740: onExecuteProcessingRequest for task: T5
		 * 04-25 21:21:15.725: onExecuteNetworkRequest for task: T12
		 * 04-25 21:21:15.745: onTaskComplete for operation: O1, T5
		 * 04-25 21:21:15.745: onTaskComplete for operation: O3, T5
		 * 04-25 21:21:15.745: onTaskStarted for operation: O3, T14
		 * 04-25 21:21:15.745: onExecuteNetworkRequest for task: T14
		 * 04-25 21:21:15.750: onExecuteProcessingRequest for task: T11
		 * 04-25 21:21:16.755: onTaskComplete for operation: O1, T11
		 * 04-25 21:21:16.760: onExecuteProcessingRequest for task: T14
		 * 04-25 21:21:17.760: onTaskComplete for operation: O3, T14
		 * 04-25 21:21:17.760: onComplete for operation: O3
		 * 04-25 21:21:17.760: Success for operation: O3
		 * 04-25 21:21:17.775: onExecuteProcessingRequest for task: T12
		 * 04-25 21:21:18.780: onTaskComplete for operation: O1, T12
		 * 04-25 21:21:18.780: onTaskStarted for operation: O1, T7
		 * 04-25 21:21:18.780: onExecuteNetworkRequest for task: T7
		 * 04-25 21:21:18.785: onExecuteProcessingRequest for task: T0
		 * 04-25 21:21:19.785: onTaskComplete for operation: O4, T0
		 * 04-25 21:21:19.785: onExecuteNetworkRequest for task: T4
		 * 04-25 21:21:19.790: onTaskStarted for operation: O4, T4
		 * 04-25 21:21:19.790: onTaskStarted for operation: O4, T3
		 * 04-25 21:21:19.795: onExecuteNetworkRequest for task: T3
		 * 04-25 21:21:19.795: onExecuteProcessingRequest for task: T7
		 * 04-25 21:21:20.795: onTaskComplete for operation: O1, T7
		 * 04-25 21:21:20.795: onComplete for operation: O1
		 * 04-25 21:21:20.795: Success for operation: O1
		 * 04-25 21:21:20.800: onExecuteProcessingRequest for task: T4
		 * 04-25 21:21:21.805: onTaskComplete for operation: O4, T4
		 * 04-25 21:21:21.805: onExecuteProcessingRequest for task: T3
		 * 04-25 21:21:22.810: onTaskComplete for operation: O4, T3
		 * 04-25 21:21:22.810: onTaskStarted for operation: O4, T5
		 * 04-25 21:21:22.810: onExecuteNetworkRequest for task: T5
		 * 04-25 21:21:23.815: onExecuteProcessingRequest for task: T5
		 * 04-25 21:21:24.820: onTaskComplete for operation: O4, T5
		 * 04-25 21:21:24.825: onTaskStarted for operation: O4, T6
		 * 04-25 21:21:24.825: onExecuteNetworkRequest for task: T6
		 * 04-25 21:21:25.830: onExecuteProcessingRequest for task: T6
		 * 04-25 21:21:26.835: onTaskComplete for operation: O4, T6
		 * 04-25 21:21:26.835: onExecuteNetworkRequest for task: T8
		 * 04-25 21:21:26.835: onTaskStarted for operation: O4, T8
		 * 04-25 21:21:26.840: onExecuteNetworkRequest for task: T9
		 * 04-25 21:21:26.840: onTaskStarted for operation: O4, T9
		 * 04-25 21:21:26.840: onTaskStarted for operation: O4, T7
		 * 04-25 21:21:27.845: onExecuteNetworkRequest for task: T7
		 * 04-25 21:21:27.845: onExecuteProcessingRequest for task: T8
		 * 04-25 21:21:28.855: onTaskComplete for operation: O4, T8
		 * 04-25 21:21:28.855: onExecuteNetworkRequest for task: T10
		 * 04-25 21:21:28.855: onTaskStarted for operation: O4, T10
		 * 04-25 21:21:28.860: onExecuteProcessingRequest for task: T7
		 * 04-25 21:21:29.860: onTaskComplete for operation: O4, T7
		 * 04-25 21:21:29.860: onExecuteProcessingRequest for task: T10
		 * 04-25 21:21:30.865: onTaskComplete for operation: O4, T10
		 * 04-25 21:21:30.865: onExecuteNetworkRequest for task: T12
		 * 04-25 21:21:30.865: onTaskStarted for operation: O4, T12
		 * 04-25 21:21:30.870: onTaskStarted for operation: O4, T11
		 * 04-25 21:21:30.875: onExecuteNetworkRequest for task: T11
		 * 04-25 21:21:30.875: onTaskStarted for operation: O4, T13
		 * 04-25 21:21:30.880: onExecuteProcessingRequest for task: T9
		 * 04-25 21:21:31.870: onExecuteNetworkRequest for task: T13
		 * 04-25 21:21:31.885: onTaskComplete for operation: O4, T9
		 * 04-25 21:21:31.885: onExecuteProcessingRequest for task: T11
		 * 04-25 21:21:32.885: onTaskComplete for operation: O4, T11
		 * 04-25 21:21:32.885: onExecuteProcessingRequest for task: T13
		 * 04-25 21:21:33.890: onTaskComplete for operation: O4, T13
		 * 04-25 21:21:33.890: onExecuteProcessingRequest for task: T12
		 * 04-25 21:21:34.895: onTaskComplete for operation: O4, T12
		 * 04-25 21:21:34.895: onExecuteNetworkRequest for task: T14
		 * 04-25 21:21:34.895: onTaskStarted for operation: O4, T14
		 * 04-25 21:21:35.900: onExecuteProcessingRequest for task: T14
		 * 04-25 21:21:36.905: onTaskComplete for operation: O4, T14
		 * 04-25 21:21:36.905: onComplete for operation: O4
		 * 04-25 21:21:36.905: Success for operation: O4
		 */
	};
	
	private final Runnable test10 = new Runnable() {
		@Override
		public void run() {
			RestService.start(mContext, new MultipleTaskOperation(0, new int[] {1, 2, 3}));
			RestService.start(mContext, new DiamondGraphOperation(1, 2, new int[] {11, 12, 5}, 7));
			RestService.start(mContext, new LinearDependencyOperation(2, new int[] {1, 7, 2}));
			RestService.start(mContext, new DiamondGraphOperation(3, 3, new int[] {14, 2, 5}, 14));
			RestService.start(mContext, new ReallyComplexOperation(4));
			RestService.start(mContext, new MultipleTaskOperation(5, new int[] {1, 2, 3}));
			RestService.start(mContext, new DiamondGraphOperation(6, 2, new int[] {11, 12, 5}, 7));
			RestService.start(mContext, new LinearDependencyOperation(7, new int[] {1, 7, 2}));
			RestService.start(mContext, new DiamondGraphOperation(8, 3, new int[] {14, 2, 5}, 14));
			RestService.start(mContext, new ReallyComplexOperation(9));
			RestService.start(mContext, new MultipleTaskOperation(10, new int[] {1, 2, 3}));
			RestService.start(mContext, new DiamondGraphOperation(11, 2, new int[] {11, 12, 5}, 7));
			RestService.start(mContext, new LinearDependencyOperation(12, new int[] {1, 7, 2}));
			RestService.start(mContext, new DiamondGraphOperation(13, 3, new int[] {14, 2, 5}, 14));
			RestService.start(mContext, new ReallyComplexOperation(14));
			RestService.start(mContext, new MultipleTaskOperation(15, new int[] {1, 2, 3}));
			RestService.start(mContext, new DiamondGraphOperation(16, 2, new int[] {11, 12, 5}, 7));
			RestService.start(mContext, new LinearDependencyOperation(17, new int[] {1, 7, 2}));
			RestService.start(mContext, new DiamondGraphOperation(18, 3, new int[] {14, 2, 5}, 14));
			RestService.start(mContext, new ReallyComplexOperation(19));
			RestService.start(mContext, new MultipleTaskOperation(20, new int[] {1, 2, 3}));
			RestService.start(mContext, new DiamondGraphOperation(21, 2, new int[] {11, 12, 5}, 7));
			RestService.start(mContext, new LinearDependencyOperation(22, new int[] {1, 7, 2}));
			RestService.start(mContext, new DiamondGraphOperation(23, 3, new int[] {14, 2, 5}, 14));
			RestService.start(mContext, new ReallyComplexOperation(24));
		}
		
		// All operations succeeded as expected.
	};

}
