package com.imdeity.deitydrops;

import com.imdeity.deityapi.DeityAPI;

public class AsyncSqlCommand  implements Runnable
{
	String _sql;
	Object[] _params;
	
	public AsyncSqlCommand(String sql, Object... params)
	{
		_sql = sql;
		_params = params;
	}
	
	@Override
	public void run() 
	{
		DeityAPI.getAPI().getDataAPI().getMySQL().write(_sql, _params);
	}

	
}
