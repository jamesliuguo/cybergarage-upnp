/******************************************************************
*
*	MediaServer for CyberLink
*
*	Copyright (C) Satoshi Konno 2003
*
*	File : AVTransport.java
*
*	Revision:
*
*	02/22/08
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.media.render;

import org.cybergarage.util.*;
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.control.*;
import org.cybergarage.upnp.media.server.object.*;

public class AVTransport implements ActionListener, QueryListener
{
	////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////

	public final static String SERVICE_TYPE = "urn:schemas-upnp-org:service:ConnectionManager:1";		
	
	// Browse Action	
	
	public final static String HTTP_GET = "http-get";
	
	public final static String GET_PROTOCOL_INFO = "GetProtocolInfo";
	public final static String SOURCE = "Source";
	public final static String SINK= "Sink";

	public final static String PREPARE_FOR_CONNECTION = "PrepareForConnection";
	public final static String REMOTE_PROTOCOL_INFO= "RemoteProtocolInfo";
	public final static String PEER_CONNECTION_MANAGER = "PeerConnectionManager";
	public final static String PEER_CONNECTION_ID = "PeerConnectionID";
	public final static String DIRECTION = "Direction";
	public final static String CONNECTION_ID = "ConnectionID";
	public final static String AV_TRNSPORT_ID = "AVTransportID";
	public final static String RCS_ID = "RcsID";
	
	public final static String CONNECTION_COMPLETE = "ConnectionComplete";

	public final static String GET_CURRENT_CONNECTION_IDS = "GetCurrentConnectionIDs";
	public final static String CONNECTION_IDS = "ConnectionIDs";

	public final static String GET_CURRENT_CONNECTION_INFO = "GetCurrentConnectionInfo";
	public final static String PROTOCOL_INFO= "ProtocolInfo";
	public final static String STATUS = "Status";
	
	public final static String SCPD = 
		"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
		"<scpd xmlns=\"urn:schemas-upnp-org:service-1-0\">\n" +
		"   <specVersion>\n" +
		"      <major>1</major>\n" +
		"      <minor>0</minor>\n" +
		"	</specVersion>\n" +
		"	<actionList>\n" +
		"		<action>\n" +
		"         <name>GetCurrentConnectionInfo</name>\n" +
		"         <argumentList>\n" +
		"            <argument>\n" +
		"               <name>ConnectionID</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_ConnectionID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>RcsID</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_RcsID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>AVTransportID</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_AVTransportID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>ProtocolInfo</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_ProtocolInfo</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>PeerConnectionManager</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_ConnectionManager</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>PeerConnectionID</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_ConnectionID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>Direction</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_Direction</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>Status</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_ConnectionStatus</relatedStateVariable>\n" +
		"            </argument>\n" +
		"         </argumentList>\n" +
		"      </action>\n" +
		"      <action>\n" +
		"         <name>GetProtocolInfo</name>\n" +
		"         <argumentList>\n" +
		"            <argument>\n" +
		"               <name>Source</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>SourceProtocolInfo</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>Sink</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>SinkProtocolInfo</relatedStateVariable>\n" +
		"            </argument>\n" +
		"         </argumentList>\n" +
		"      </action>\n" +
		"      <action>\n" +
		"         <name>GetCurrentConnectionIDs</name>\n" +
		"         <argumentList>\n" +
		"            <argument>\n" +
		"               <name>ConnectionIDs</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>CurrentConnectionIDs</relatedStateVariable>\n" +
		"            </argument>\n" +
		"         </argumentList>\n" +
		"      </action>\n" +
		"   </actionList>\n" +
		"   <serviceStateTable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_ProtocolInfo</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_ConnectionStatus</name>\n" +
		"         <dataType>string</dataType>\n" +
		"         <allowedValueList>\n" +
		"            <allowedValue>OK</allowedValue>\n" +
		"            <allowedValue>ContentFormatMismatch</allowedValue>\n" +
		"            <allowedValue>InsufficientBandwidth</allowedValue>\n" +
		"            <allowedValue>UnreliableChannel</allowedValue>\n" +
		"            <allowedValue>Unknown</allowedValue>\n" +
		"         </allowedValueList>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_AVTransportID</name>\n" +
		"         <dataType>i4</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_RcsID</name>\n" +
		"         <dataType>i4</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_ConnectionID</name>\n" +
		"         <dataType>i4</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_ConnectionManager</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"yes\">\n" +
		"         <name>SourceProtocolInfo</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"yes\">\n" +
		"         <name>SinkProtocolInfo</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_Direction</name>\n" +
		"         <dataType>string</dataType>\n" +
		"         <allowedValueList>\n" +
		"            <allowedValue>Input</allowedValue>\n" +
		"            <allowedValue>Output</allowedValue>\n" +
		"         </allowedValueList>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"yes\">\n" +
		"         <name>CurrentConnectionIDs</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"   </serviceStateTable>\n" +
		"</scpd>";	

	////////////////////////////////////////////////
	// Constructor 
	////////////////////////////////////////////////
	
	public ConnectionManager(MediaServer mserver)
	{
		setMediaServer(mserver);
		maxConnectionID = 0;
	}
	
	////////////////////////////////////////////////
	// Media Server
	////////////////////////////////////////////////

	private MediaServer mediaServer;
	
	private void setMediaServer(MediaServer mserver)
	{
		mediaServer = mserver;	
	}
	
	public MediaServer getMediaServer()
	{
		return mediaServer;	
	}

	public ContentDirectory getContentDirectory()
	{
		return getMediaServer().getContentDirectory();	
	}
	
	////////////////////////////////////////////////
	// Mutex
	////////////////////////////////////////////////
	
	private Mutex mutex = new Mutex();
	
	public void lock()
	{
		mutex.lock();
	}
	
	public void unlock()
	{
		mutex.unlock();
	}
	
	////////////////////////////////////////////////
	// ConnectionID
	////////////////////////////////////////////////
	
	private int maxConnectionID;
	
	public int getNextConnectionID()
	{
		lock();
		maxConnectionID++;
		unlock();
		return maxConnectionID;
	}
	
	////////////////////////////////////////////////
	// ConnectionInfoList
	////////////////////////////////////////////////
	
	// Thanks for Brian Owens (12/02/04)
	private ConnectionInfoList conInfoList = new ConnectionInfoList();;
	
	public ConnectionInfoList getConnectionInfoList()
	{
		return conInfoList;
	}
	
	public ConnectionInfo getConnectionInfo(int id)
	{
		int size = conInfoList.size();
		for (int n=0; n<size; n++) {
			ConnectionInfo info = conInfoList.getConnectionInfo(n);
			if (info.getID() == id)
				return info;
		}
		return null;
	}
	
	public void addConnectionInfo(ConnectionInfo info)
	{
		lock();
		conInfoList.add(info);
		unlock();
	}
	
	public void removeConnectionInfo(int id)
	{
		lock();
		int size = conInfoList.size();
		for (int n=0; n<size; n++) {
			ConnectionInfo info = conInfoList.getConnectionInfo(n);
			if (info.getID() == id) {
				conInfoList.remove(info);
				break;
			}
		}
		unlock();
	}
	
	public void removeConnectionInfo(ConnectionInfo info)
	{
		lock();
		conInfoList.remove(info);
		unlock();
	}
	
	////////////////////////////////////////////////
	// ActionListener
	////////////////////////////////////////////////

	public boolean actionControlReceived(Action action)
	{
		//action.print();
		
		String actionName = action.getName();
		
		if (actionName.equals(GET_PROTOCOL_INFO) == true) {
			// Source
			String sourceValue = "";
			int mimeTypeCnt = getContentDirectory().getNFormats();
			for (int n=0; n<mimeTypeCnt; n++) {
				if (0 < n)
					sourceValue += ",";
				Format format = getContentDirectory().getFormat(n);
				String mimeType = format.getMimeType();
				sourceValue += HTTP_GET + ":*:" + mimeType + ":*";
			}
			action.getArgument(SOURCE).setValue(sourceValue);
			// Sink
			action.getArgument(SINK).setValue("");
			return true;
		}

		if (actionName.equals(PREPARE_FOR_CONNECTION) == true) {
			action.getArgument(CONNECTION_ID).setValue(-1);
			action.getArgument(AV_TRNSPORT_ID).setValue(-1);
			action.getArgument(RCS_ID).setValue(-1);
			return true;
		}
		
		if (actionName.equals(CONNECTION_COMPLETE) == true) {
			return true;
		}
		
		if (actionName.equals(GET_CURRENT_CONNECTION_INFO) == true)
			return getCurrentConnectionInfo(action);
		
		if (actionName.equals(GET_CURRENT_CONNECTION_IDS) == true)
			return getCurrentConnectionIDs(action);
		
		return false;
	}

	////////////////////////////////////////////////
	// GetCurrentConnectionIDs
	////////////////////////////////////////////////
	
	private boolean getCurrentConnectionIDs(Action action)
	{
		String conIDs = "";
		lock();
		int size = conInfoList.size();
		for (int n=0; n<size; n++) {
			ConnectionInfo info = conInfoList.getConnectionInfo(n);
			if (0 < n)
				conIDs += ",";
			conIDs += Integer.toString(info.getID());
		}
		action.getArgument(CONNECTION_IDS).setValue(conIDs);
		unlock();
		return true;
	}
	
	////////////////////////////////////////////////
	// GetCurrentConnectionInfo
	////////////////////////////////////////////////
	
	private boolean getCurrentConnectionInfo(Action action)
	{
		int id = action.getArgument(RCS_ID).getIntegerValue();
		lock();
		ConnectionInfo info = getConnectionInfo(id);
		if (info != null) {
			action.getArgument(RCS_ID).setValue(info.getRcsID());
			action.getArgument(AV_TRNSPORT_ID).setValue(info.getAVTransportID());
			action.getArgument(PEER_CONNECTION_MANAGER).setValue(info.getPeerConnectionManager());
			action.getArgument(PEER_CONNECTION_ID).setValue(info.getPeerConnectionID());
			action.getArgument(DIRECTION).setValue(info.getDirection());
			action.getArgument(STATUS).setValue(info.getStatus());
		}
		else {
			action.getArgument(RCS_ID).setValue(-1);
			action.getArgument(AV_TRNSPORT_ID).setValue(-1);
			action.getArgument(PEER_CONNECTION_MANAGER).setValue("");
			action.getArgument(PEER_CONNECTION_ID).setValue(-1);
			action.getArgument(DIRECTION).setValue(ConnectionInfo.OUTPUT);
			action.getArgument(STATUS).setValue(ConnectionInfo.UNKNOWN);
		}
		unlock();
		return true;
	}
	
	////////////////////////////////////////////////
	// QueryListener
	////////////////////////////////////////////////

	public boolean queryControlReceived(StateVariable stateVar)
	{
		return false;
	}
}

