/**
 * api form generate
 */
!function($, g) {
	var utils = {
		isArray : function(obj) {
			return Object.prototype.toString.call(obj) === '[object Array]';
		},
		isString : function(obj) {
			return Object.prototype.toString.call(obj) === '[object String]';
		},
		isObject : function(obj) {
			return Object.prototype.toString.call(obj) === '[object Object]';
		},
		isFunction : function(obj) {
			return Object.prototype.toString.call(obj) === '[object Function]';
		},
		getQueryString : function(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
			var r = window.location.search.substr(1).match(reg);
			if (r != null) {
				return unescape(r[2]);
			} else {
				return null;
			}
		},
		/**
		 * 缓存数据
		 * 
		 * @param {type}
		 *            key 键值
		 * @param {type}
		 *            val 数据
		 * @param {type}
		 *            expire 过期时间
		 * @returns {undefined}
		 */
		setLocalVal : function(key, val, expire) {
			if (!window.localStorage) {
				return false;
			}
			if (!key)
				return false;
			var userID = window.Yii_UserID || 0;
			key = key + '_' + userID;
			if (expire && expire > 0) {
				// 当前时间戳
				var timestamp = parseInt(Date.parse(new Date()) / 1000);
				var newtime = timestamp + expire;
				window.localStorage.setItem(key, JSON.stringify({
					v : val,
					exp : newtime
				}));
			} else {
				window.localStorage.setItem(key, JSON.stringify({
					v : val
				}));
			}
		},
		getLocalVal : function(key) {
			if (!window.localStorage) {
				return false;
			}
			if (!key)
				return false;
			var userID = window.Yii_UserID || 0;
			key = key + '_' + userID;
			var val = JSON.parse(window.localStorage.getItem(key));
			if (val == null) {
				return '';
			}
			if (val && val.exp) {
				// 当前时间戳
				var current = parseInt(Date.parse(new Date()) / 1000);
				if (current < val.exp) {
					return val.v;
				} else {
					// 删除过期缓存
					window.localStorage.removeItem(key);
					return '';
				}
			} else {
				return val.v;
			}
		},
		getKey : function(key) {
			var idx = key.indexOf('<');
			if (idx > -1) {
				key = key.substring(0,idx);
			}
			return key;
		}
	};
	var apiform = {
		_cacheKey : 'headparams',
		_apiUrl : '/jiaparts-support-api/',
		_className : {
			simpleForm : 'simpleForm',
			javaBean : 'javaBean',
			javaBeanList : 'javaBeanList',
			javaBeanListInput : 'javaBeanListInput',
			linearArray : 'linearArray',
			map : 'map',
			mapKey : 'mapKey',
			mapValue : 'mapValue',
			mapTr : 'mapTr'
		},
		_postUrl : '',
		_requestParams : {},// 请求参数
		_javaBeanArr : [],
		_javaBeanListArr : [],
		/**
		 * 初始化方法
		 */
		init : function() {
			this._initVar();
			this._generateForm();
			this._event();
			this._setPostUrl();
		},
		_initVar : function() {
			this.$headparams = $('#headparams');
			this.$bodyparams = $('#bodyparamsdiv');
			this.$simpleparams = $('#simpleparams');
			this.$javaBeanTmpl = $('#javaBeanTmpl');
			this.$mapTmpl = $('#mapTmpl');
			this.$javaBeanListTmpl = $('#javaBeanListTmpl');
			this.$linearArrayTmpl = $('#linearArrayTmpl');
			this.$submitform = $('#submitform');
			this.$inputs = $('#inputs');
			this.$result = $('#result');
			var value = this.$inputs.val();
			var data = JSON.parse(value);
			this._requestParams = data;
			console.log(this._requestParams);
		},
		/**
		 * 设置请求url
		 */
		_setPostUrl : function() {
			var coreName = utils.getQueryString('coreName');
			var method = utils.getQueryString('method');
			var pathname = location.pathname;
			var arr = pathname.split('/');
			var url = location.protocol + '//' + location.host + '/' + arr[1]
					+ '/' + coreName + '/' + method;
			this._postUrl = url;
		},
		/**
		 * 事件
		 */
		_event : function() {
			var self = this;
			/**
			 * 添加行
			 */
			$(document).on('click', '.addrow', function() {
				var $this = $(this), type = $this.attr('htype');
				var javaBeanList = '.' + self._className.javaBeanList;
				if (type == 2) {
					javaBeanList = '.' + self._className.linearArray;
				} else if (type == 3) {
					javaBeanList = '.' + self._className.map;
				}
				var $tbody = $this.parents(javaBeanList);
				var $curRow = $this.parent('td').parent('tr');
				$tbody.append($curRow.clone());
				$tbody.find('tr:last').find('input[type="text"]').val('');
			});
			/**
			 * 删除行
			 */
			$(document).on('click', '.delrow', function() {
				var $this = $(this), type = $this.attr('htype');
				var javaBeanList = '.' + self._className.javaBeanList;
				if (type == 2) {
					javaBeanList = '.' + self._className.linearArray;
				} else if (type == 3) {
					javaBeanList = '.' + self._className.map;
				}
				var $tbody = $this.parents(javaBeanList);
				var $curRow = $this.parent('td').parent('tr');
				if ($tbody.find('tr').length > 1) {
					$curRow.remove();
				}
			});
			/**
			 * 表单提交
			 */
			this.$submitform.click(function() {
				// 获取头参数
				try {
					var data = self._getHeadParams(), body = self
							._getBodyParams();
				} catch (error) {
					console.log(error);
					alert('获取参数出错!');
					return;
				}
				var head = $.extend(true, {}, data);
				data.param = body;
				console.log(data);
				self._submit(data, function(res) {
					self._saveHeadParamsCache(head);
					var val = js_beautify(JSON.stringify(res), 2, ' ');
					self.$result.val(val);
				});
				return false;
			});
		},
		/**
		 * 生成表单
		 */
		_generateForm : function() {
			try {
				this._createHeadParams();
				this._createBodyParams();
			} catch (error) {
				console.log(error);
				alert('生成表单出错!');
			}
		},
		/**
		 * 创建请求头表单参数
		 */
		_createHeadParams : function() {
			var fileds = this._requestParams.head;
			var cache = this._getHeadParamsCache();
			console.log(cache);
			var i = 0, html = '';
			for (i in fileds) {
				if (fileds.hasOwnProperty(i)) {
					html += this._createSimpleFormElement(i, i, cache[i]);
				}
			}
			this.$headparams.html(html);
		},
		/**
		 * 创建请求主体表单参数
		 * 
		 * @param fileds
		 */
		_createBodyParams : function() {
			var fileds = this._requestParams.param, html = '', simplehtml = '';
			for (i in fileds) {
				if (!fileds.hasOwnProperty(i)) {
					continue;
				}
				if (utils.isString(fileds[i])) {
					// 简单元素
					simplehtml += this._createSimpleFormElement(i);
				} else if (utils.isObject(fileds[i])) {
					console.log(i,fileds[i]);
					// javabean
					html += this._createJavaBeanFormElement(i, fileds[i]);
					this._javaBeanArr.push(i);
				} else if (utils.isArray(fileds[i])) {
					if (utils.isString(fileds[i][0])) {
						// 一维数组
						html += this
								._createLinearArrayFormElement(i, fileds[i]);
					} else {
						// javabeanlist
						html += this._createJavaBeanListFormElement(i,
								fileds[i]);
						this._javaBeanListArr.push(i);
					}
				}
			}
			this.$simpleparams.prepend(simplehtml);
			this.$bodyparams.append(html);
		},
		/**
		 * 创建简单表单元素 key->value形式
		 */
		_createSimpleFormElement : function(key, desc, val) {
			desc || (desc = key);
			val === undefined && (val = '');
			var str = '<tr><td>' + desc + '</td><td><input class="'
					+ this._className.simpleForm + '" type="textfield" name="'
					+ utils.getKey(key) + '" value="' + val + '"></td></tr>';
			return str;
		},
		/**
		 * 创建Java Bean List Form类型表单元素
		 */
		_createJavaBeanListFormElement : function(key, lists) {
			var data = {
				key : key,
				fileds : lists
			};
			var el = this.$javaBeanListTmpl.tmpl(data);
			return $('<div></div>').append(el).html();
		},
		/**
		 * 创建Java Bean Form类型表单元素
		 */
		_createJavaBeanFormElement : function(key, lists) {
			var data = {
				key : key,
			};
			var len = 0, fileds = [];
			for ( var i in lists) {
				if (lists.hasOwnProperty(i)) {
					len++;
					fileds.push(i);
				}
			}
			if (len == 0) {
				// map
				return this._createMapFormElement(key);
			}
			data.fileds = fileds;
			data.len = len;
			var el = this.$javaBeanTmpl.tmpl(data);
			return $('<div></div>').append(el).html();
		},
		/**
		 * 创建map
		 */
		_createMapFormElement : function(key) {
			var data = {
				key : key,
			};
			var el = this.$mapTmpl.tmpl(data);
			return $('<div></div>').append(el).html();
		},
		/**
		 * 创建一维数组表单
		 * 
		 * @param key
		 */
		_createLinearArrayFormElement : function(key) {
			var el = this.$linearArrayTmpl.tmpl({
				key : key
			});
			return $('<div></div>').append(el).html();
		},
		/**
		 * 获取头参数
		 * 
		 * @returns {___anonymous2878_2879}
		 */
		_getHeadParams : function() {
			var data = {}, name = '.' + this._className.simpleForm;
			this.$headparams.find(name).each(function() {
				var $this = $(this);
				data[$this.attr('name')] = $.trim($this.val());
			});
			return data;
		},
		/**
		 * 获取body参数
		 * 
		 * @returns {___anonymous3233_3234}
		 */
		_getBodyParams : function() {
			var data = {}, name = '.' + this._className.simpleForm;
			// 获取简单数据
			this.$bodyparams.find(name).each(function() {
				var $this = $(this);
				data[$this.attr('name')] = $.trim($this.val());
			});
			// 获取list数据
			var javaBeanListData = this._getBodyJavaBeanListParams();
			var javaBeanData = this._getBodyJavaBeanParams();
			var linearArrayData = this._getBodyLinearArrayParams();
			var mapData = this._getBodyMapParams();
			$.extend(data, javaBeanListData, javaBeanData, linearArrayData,
					mapData);
			return data;
		},
		/**
		 * 获取JavaBeanList参数
		 */
		_getBodyJavaBeanListParams : function() {
			var data = {};
			var javaBeanList = '.' + this._className.javaBeanList;
			var javaBeanListInput = '.' + this._className.javaBeanListInput;
			// 循环列表
			this.$bodyparams.find(javaBeanList).each(function() {
				// 表单
				var $this = $(this), key = utils.getKey($this.attr('key'));
				data[key] = [];
				// 循环表行
				$this.find('tr').each(function() {
					// 循环input
					var item = {}, exists = 0;
					$(this).find(javaBeanListInput).each(function() {
						var val = $.trim($(this).val());
						if (val !== '') {
							item[$(this).attr('name')] = val;
							exists = 1;
						}
					});
					if (exists) {
						data[key].push(item);
					}
				});
			});
			return data;
		},
		/**
		 * 获取JavaBean参数
		 * 
		 * @returns {___anonymous6397_6398}
		 */
		_getBodyJavaBeanParams : function() {
			var data = {};
			var javaBean = '.' + this._className.javaBean;
			var javaBeanListInput = '.' + this._className.javaBeanListInput;
			// 循环列表
			this.$bodyparams.find(javaBean).each(function() {
				// 表单
				var $this = $(this), item = {};
				$this.find(javaBeanListInput).each(function() {
					var val = $.trim($(this).val());
					item[$(this).attr('name')] = val;
				});
				data[utils.getKey($this.attr('key'))] = item;
			});
			return data;
		},
		/**
		 * 获取JavaBean参数
		 * 
		 * @returns {___anonymous6397_6398}
		 */
		_getBodyLinearArrayParams : function() {
			var data = {};
			var javaBean = '.' + this._className.linearArray;
			var javaBeanListInput = '.' + this._className.javaBeanListInput;
			// 循环列表
			this.$bodyparams.find(javaBean).each(function() {
				// 表单
				var $this = $(this), item = [];
				$this.find(javaBeanListInput).each(function() {
					var val = $.trim($(this).val());
					if (val !== '') {
						item.push(val);
					}
				});
				data[utils.getKey($this.attr('key'))] = item;
			});
			return data;
		},
		/**
		 * 获取map参数
		 */
		_getBodyMapParams : function() {
			var data = {};
			var mapClass = '.' + this._className.map;
			var mapKey = '.' + this._className.mapKey;
			var mapValue = '.' + this._className.mapValue;
			var mapTr = '.' + this._className.mapTr;
			// 循环列表
			this.$bodyparams.find(mapClass).each(function() {
				// 表单
				var $this = $(this), item = {};
				$this.find(mapTr).each(function() {
					var $this = $(this);
					var key = $.trim($this.find(mapKey).val());
					var val = $.trim($this.find(mapValue).val());
					if (key !== '') {
						item[key] = val;
					}
				});
				data[utils.getKey($this.attr('key'))] = item;
			});
			return data;
		},
		/**
		 * 提交
		 * 
		 * @param data
		 */
		_submit : function(data, cb) {
			utils.isFunction(cb) || (cb = $.noop);
			
			//去掉注解
			var sdata = {};
			for (var k in data) {  
				var v = data[k];  
				var idx =k.indexOf('<');
				if (idx > -1) {
					k = k.substring(0,idx);
				}
				sdata[k]= v;
			}
			
			
			$.ajax({
				url : this._postUrl,
				dataType : 'json',
				data : {
					body : JSON.stringify(sdata)
				},
				type : 'post',
				success : function(res) {
					cb(res);
				},
				error : function() {
					alert('请求出错');
				},
				complete : function() {

				}
			});
		},
		/**
		 * 保存head参数缓存
		 */
		_saveHeadParamsCache : function(data) {
			utils.setLocalVal(this._cacheKey, data);
		},
		/**
		 * 获取head参数缓存
		 */
		_getHeadParamsCache : function() {
			var cache = utils.getLocalVal(this._cacheKey);
			return cache || {};
		},
	};
	g.apiform = apiform;
}(jQuery, window);

/**
 * 启动
 */
$(function() {
	window.apiform && apiform.init();
});