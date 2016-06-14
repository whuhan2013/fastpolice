package com.zj.fastpolice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption.DrivingPolicy;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRoutePlanOption.TransitPolicy;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import overlayutil.DrivingRouteOverlay;
import overlayutil.TransitRouteOverlay;
import overlayutil.WalkingRouteOverlay;

/**
 * 路线规划
 *
 * @author ys
 *
 */
public class RoutePlanningActivity extends Activity implements OnClickListener {

	private MapView mapView;
	private BaiduMap bdMap;

	private EditText startEt;
	private EditText endEt;

	private String startPlace;// 开始地点
	private String endPlace;// 结束地点

	private Button driveBtn;// 驾车
	private Button walkBtn;// 步行
	private Button transitBtn;// 换成 （公交）
	private Button nextLineBtn;

	private Spinner drivingSpinner, transitSpinner;

	private RoutePlanSearch routePlanSearch;// 路径规划搜索接口

	private int index = -1;
	private int totalLine = 0;// 记录某种搜索出的方案数量
	private int drivintResultIndex = 0;// 驾车路线方案index
	private int transitResultIndex = 0;// 换乘路线方案index
    String destination;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_planning);

		init();
	}

	/**
	 *
	 */
	private void init() {
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.showZoomControls(false);
		bdMap = mapView.getMap();

		startEt = (EditText) findViewById(R.id.start_et);
		endEt = (EditText) findViewById(R.id.end_et);
		driveBtn = (Button) findViewById(R.id.drive_btn);
		transitBtn = (Button) findViewById(R.id.transit_btn);
		walkBtn = (Button) findViewById(R.id.walk_btn);
		nextLineBtn = (Button) findViewById(R.id.nextline_btn);
		nextLineBtn.setEnabled(false);
		driveBtn.setOnClickListener(this);
		transitBtn.setOnClickListener(this);
		walkBtn.setOnClickListener(this);
		nextLineBtn.setOnClickListener(this);


		Intent intent=getIntent();
		destination=intent.getStringExtra("destination");
		startEt.setText("武汉大学");
		endEt.setText(destination);

		drivingSpinner = (Spinner) findViewById(R.id.driving_spinner);
		String[] drivingItems = getResources().getStringArray(
				R.array.driving_spinner);
		ArrayAdapter<String> drivingAdapter = new ArrayAdapter<>(this,
				android.R.layout.simple_spinner_item, drivingItems);
		drivingSpinner.setAdapter(drivingAdapter);
		drivingSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				if (index == 0) {
					drivintResultIndex = 0;
					drivingSearch(drivintResultIndex);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		transitSpinner = (Spinner) findViewById(R.id.transit_spinner);
		String[] transitItems = getResources().getStringArray(
				R.array.transit_spinner);
		ArrayAdapter<String> transitAdapter = new ArrayAdapter<>(this,
				android.R.layout.simple_spinner_item, transitItems);
		transitSpinner.setAdapter(transitAdapter);
		transitSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				if (index == 1) {
					transitResultIndex = 0;
					transitSearch(transitResultIndex);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		routePlanSearch = RoutePlanSearch.newInstance();
		routePlanSearch
				.setOnGetRoutePlanResultListener(routePlanResultListener);
	}

	/**
	 * 路线规划结果回调
	 */
	OnGetRoutePlanResultListener routePlanResultListener = new OnGetRoutePlanResultListener() {

		/**
		 * 步行路线结果回调
		 */
		@Override
		public void onGetWalkingRouteResult(
				WalkingRouteResult walkingRouteResult) {
			bdMap.clear();
			if (walkingRouteResult == null
					|| walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(RoutePlanningActivity.this, "抱歉，未找到结果",
						Toast.LENGTH_SHORT).show();
			}
			if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// TODO
				return;
			}
			if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
				WalkingRouteOverlay walkingRouteOverlay = new WalkingRouteOverlay(
						bdMap);
				walkingRouteOverlay.setData(walkingRouteResult.getRouteLines()
						.get(drivintResultIndex));
				bdMap.setOnMarkerClickListener(walkingRouteOverlay);
				walkingRouteOverlay.addToMap();
				walkingRouteOverlay.zoomToSpan();
				totalLine = walkingRouteResult.getRouteLines().size();
				Toast.makeText(RoutePlanningActivity.this,
						"共查询出" + totalLine + "条符合条件的线路", Toast.LENGTH_SHORT).show();
				if (totalLine > 1) {
					nextLineBtn.setEnabled(true);
				}
			}
		}

		/**
		 * 换成路线结果回调
		 */
		@Override
		public void onGetTransitRouteResult(
				TransitRouteResult transitRouteResult) {
			bdMap.clear();
			if (transitRouteResult == null
					|| transitRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(RoutePlanningActivity.this, "抱歉，未找到结果",
						Toast.LENGTH_SHORT).show();
			}
			if (transitRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				// drivingRouteResult.getSuggestAddrInfo()
				return;
			}
			if (transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
				TransitRouteOverlay transitRouteOverlay = new TransitRouteOverlay(
						bdMap);
				transitRouteOverlay.setData(transitRouteResult.getRouteLines()
						.get(drivintResultIndex));// 设置一条驾车路线方案
				bdMap.setOnMarkerClickListener(transitRouteOverlay);
				transitRouteOverlay.addToMap();
				transitRouteOverlay.zoomToSpan();
				totalLine = transitRouteResult.getRouteLines().size();
				Toast.makeText(RoutePlanningActivity.this,
						"共查询出" + totalLine + "条符合条件的线路", Toast.LENGTH_SHORT).show();
				if (totalLine > 1) {
					nextLineBtn.setEnabled(true);
				}
				// 通过getTaxiInfo()可以得到很多关于打车的信息
				Toast.makeText(
						RoutePlanningActivity.this,
						"该路线打车总路程"
								+ transitRouteResult.getTaxiInfo()
								.getDistance(), Toast.LENGTH_SHORT).show();
			}
		}

		/**
		 * 驾车路线结果回调 查询的结果可能包括多条驾车路线方案
		 */
		@Override
		public void onGetDrivingRouteResult(
				DrivingRouteResult drivingRouteResult) {
			bdMap.clear();
			Log.i("test", "test1");
			if (drivingRouteResult == null
					|| drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(RoutePlanningActivity.this, "抱歉，未找到结果",
						Toast.LENGTH_SHORT).show();
			}
			Log.i("test", "test2");
			if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				// drivingRouteResult.getSuggestAddrInfo()
				return;
			}
			if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
				DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
						bdMap);
				Log.i("test", "test3");
				drivingRouteOverlay.setData(drivingRouteResult.getRouteLines()
						.get(drivintResultIndex));// 设置一条驾车路线方案
				bdMap.setOnMarkerClickListener(drivingRouteOverlay);
				drivingRouteOverlay.addToMap();
				drivingRouteOverlay.zoomToSpan();
				totalLine = drivingRouteResult.getRouteLines().size();
				Toast.makeText(RoutePlanningActivity.this,
						"共查询出" + totalLine + "条符合条件的线路", Toast.LENGTH_SHORT).show();
				if (totalLine > 1) {
					nextLineBtn.setEnabled(true);
				}

				//Log.i("test",drivingRouteResult.getTaxiInfo().getDistance()+"");
				// 通过getTaxiInfo()可以得到很多关于打车的信息
				//Toast.makeText(RoutePlanningActivity.this,"该路线打车总路程"+ drivingRouteResult.getTaxiInfo().getDistance(), 1000).show();
			}
		}

		@Override
		public void onGetBikingRouteResult(BikingRouteResult arg0) {
			// TODO Auto-generated method stub

			Log.i("test", "test5");

		}
	};

	/**
	 * 驾车线路查询
	 */
	private void drivingSearch(int index) {
		DrivingRoutePlanOption drivingOption = new DrivingRoutePlanOption();
		drivingOption.policy(DrivingPolicy.values()[drivingSpinner
				.getSelectedItemPosition()]);// 设置驾车路线策略
		drivingOption.from(PlanNode.withCityNameAndPlaceName("北京", startPlace));// 设置起点
		drivingOption.to(PlanNode.withCityNameAndPlaceName("北京", endPlace));// 设置终点
		routePlanSearch.drivingSearch(drivingOption);// 发起驾车路线规划
	}

	/**
	 * 换乘路线查询
	 */
	private void transitSearch(int index) {
		TransitRoutePlanOption transitOption = new TransitRoutePlanOption();
		transitOption.city("北京");// 设置换乘路线规划城市，起终点中的城市将会被忽略
		transitOption.from(PlanNode.withCityNameAndPlaceName("北京", startPlace));
		transitOption.to(PlanNode.withCityNameAndPlaceName("北京", endPlace));
		transitOption.policy(TransitPolicy.values()[transitSpinner
				.getSelectedItemPosition()]);// 设置换乘策略
		routePlanSearch.transitSearch(transitOption);
	}

	/**
	 * 步行路线查询
	 */
	private void walkSearch() {
		WalkingRoutePlanOption walkOption = new WalkingRoutePlanOption();
		walkOption.from(PlanNode.withCityNameAndPlaceName("北京", startPlace));
		walkOption.to(PlanNode.withCityNameAndPlaceName("北京", endPlace));
		routePlanSearch.walkingSearch(walkOption);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.drive_btn:// 驾车
				index = 0;
				drivintResultIndex = 0;
				startPlace = startEt.getText().toString();
				endPlace = endEt.getText().toString();
				driveBtn.setEnabled(false);
				transitBtn.setEnabled(true);
				walkBtn.setEnabled(true);
				nextLineBtn.setEnabled(false);
				drivingSearch(drivintResultIndex);
				break;
			case R.id.transit_btn:// 换乘
				index = 1;
				transitResultIndex = 0;
				startPlace = startEt.getText().toString();
				endPlace = endEt.getText().toString();
				transitBtn.setEnabled(false);
				driveBtn.setEnabled(true);
				walkBtn.setEnabled(true);
				nextLineBtn.setEnabled(false);
				transitSearch(transitResultIndex);
				break;
			case R.id.walk_btn:// 步行
				index = 2;
				startPlace = startEt.getText().toString();
				endPlace = endEt.getText().toString();
				walkBtn.setEnabled(false);
				driveBtn.setEnabled(true);
				transitBtn.setEnabled(true);
				nextLineBtn.setEnabled(false);
				walkSearch();
				break;
			case R.id.nextline_btn:// 下一条
				switch (index) {
					case 0:
						drivingSearch(++drivintResultIndex);
						break;
					case 1:
						transitSearch(transitResultIndex);
						break;
					case 2:

						break;
				}
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		routePlanSearch.destroy();// 释放检索实例
		mapView.onDestroy();
	}

}
