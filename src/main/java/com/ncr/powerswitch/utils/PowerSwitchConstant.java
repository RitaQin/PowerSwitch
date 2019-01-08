package com.ncr.powerswitch.utils;

/***
 * PowerSwitch全局变量
 * 
 * @author rq185015
 */

public class PowerSwitchConstant {

	// 验证端机ip开关
	public final static String VAL_OPEN = "bool";

	// 设置加密机超时时间 单位：秒
	public final static String HSM_TIME_OUT = "20";

	// 全局的错误代码
	public final static String GLOBAL_SUCCESS_CODE = "0000"; // 成功
	public final static String GLOBAL_ERROR_CODE_PARAMS_NOT_NULL = "1001"; // 参数不能为空
	public final static String GLOBAL_ERROR_CODE_TERMINAL_ID_INVALID = "1002"; // 终端号无效
	public final static String GLOBAL_ERROR_CODE_UNKNOWN_ERROR = "1003"; // 未知错误
	public final static String GLOBAL_ERROR_CODE_TERMID_ILLEGAL = "1004"; // 非法终端号
	public final static String GLOBAL_ERROR_CODE_INFO_VAL_IP = "1005"; // 验证IP格式是否正确
	public final static String GLOBAL_ERROR_CODE_ERROR_IP_ILLEGAL = "1006"; // IP格式不正确
	public final static String GLOBAL_ERROR_CODE_TERMID_IP_FALSE = "1007"; // 端机号与IP地址不匹配
	public final static String SOCKET_TIMEOUT_ERROR_CODE = "00000000"; // socket超时
	public final static String SOCKET_UNKNOWNHOST_ERROR_CODE = "00000001"; // 未知主机
	public final static String SOCKET_CLIENT_CLOSE_ERROR_CODE = "00000002"; // 客户端关闭
	public final static String SOCKET_UNKNOWN_ERROR_CODE = "00000098"; // 未知错误
	public final static String SOCKET_SUCCESS_CODE = "00000099"; // 成功

	public final static String INDEX_CODE = "indexcode";// 加密机索引号（配置）

	// 远程申请密钥错误代码
	public final static String REMOTEKEYLOAD_DEBUG_PARAM_LOG = "remotekeyload"; // 记录远程申请密钥接口参数
	public final static String REMOTEKEYLOAD_ERROR_CODE_BATCH_INVSLID = "2001"; // 批次号无效
	public final static String REMOTEKEYLOAD_ERROR_CODE_SIGNATURE_FLASE = "2002"; // 签名校验失败
	public final static String REMOTEKEYLOAD_ERROR_CODE_ZHUMIYAO_TO_GONGYAOJIAMI = "2003"; // 主密钥转由公钥加密失败
	public final static String REMOTEKEYLOAD_ERROR_CODE_RETURN_JIAMIMIWEN = "2004"; // 返回加密密文错误
	public final static String REMOTEKEYLOAD_ERROR_CODE_MIWEN_SIYAO_SIGNATURE = "2005"; // 转加密密文用私钥签名失败
	public final static String REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR = "2006"; // 读取加密机配置参数失败
	public final static String GLOBAL_ERROR_CODE_SOCKET_ERROR = "2007"; // 通信加密机失败
	public final static String GLOBAL_ERROR_CODE_SOCKET_SUCCESS = "2008"; // 通信加密机成功
	public final static String GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_SUCCESS = "2009"; // 加密机返回数据成功
	public final static String GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE = "2010"; // 加密机返回数据失败
	public final static String GLOBAL_INFO_CODE_UPDATE_ATM_KEY_SUCCESS = "2011"; // 更新ATM密钥与密钥校验码成功
	public final static String GLOBAL_INFO_CODE_UPDATE_ATM_KEY_FALSE = "2012"; // 更新ATM密钥与密钥校验码失败
	public final static String GLOBAL_ERROR_CODE_UPDATE_ATM_KEY_FALSE = "2012"; // 更新ATM密钥与密钥校验码失败
	public final static String REMOTEKEYLOAD_ERROR_CODE_HSM_DATA_ERROR = "2013"; // 获取ATM厂商密钥配置参数失败
	public final static String REMOTEKEYLOAD_INFO_CODE_VAL_SIGN_SUCCESS = "2014"; // 验证签名成功
	public final static String REMOTEKEYLOAD_INFO_CODE_VAL_SIGN_FALSE = "2015"; // 验证签名失败
	public final static String REMOTEKEYLOAD_ERROR_PK_DATA_ILLEGAL = "2016"; // 非法公钥数据
	public final static String REMOTEKEYLOAD_ERROR_SIGN_DATA_ILLEGAL = "2017"; // 非法签名数据
	public final static String REMOTEKEYLOAD_ERROR_KEY_INDEX_ERROR = "2018"; // 无法获取索引数据
	public final static String REMOTEKEYLOAD_ERROR_MANU_PK_DATA = "2019"; // 无法获取厂商公钥数据
	public final static String REMOTEKEYLOAD_ERROR_MANU_PK_DATA_LENGTH = "2020"; // 无法获取厂商公钥长度数据
	public final static String REMOTEKEYLOAD_ERROR_ATM_PK_DATA = "2021"; // 无法获取ATM密钥数据
	public final static String REMOTEKEYLOAD_ERROR_APPLY_PK_FALSE = "2022"; // 申请密钥操作失败
	public final static String REMOTEKEYLOAD_ERROR_BANK_PK_DATA = "2023"; // 无法获取银行公钥数据
	public final static String REMOTEKEYLOAD_ERROR_BANK_PK_MANU_SIGN_DATA = "2024"; // 无法获取银行公钥的厂商签名数据
	public final static String REMOTEKEYLOAD_ERROR_SK_LENGTH_DATA = "2025"; // 无法获取银行私钥数据的长度
	public final static String REMOTEKEYLOAD_ERROR_SK__DATA = "2026"; // 无法获取银行私钥数据
	public final static String REMOTEKEYLOAD_ERROR_UPTATE_EPPID = "2027"; // 更新EPP序列号失败
	public final static String REMOTEKEYLOAD_ERROR_EPPID_COMPARE_FALSE = "2028"; // EPP序列号与端机EPP序列号不匹配，拒绝操作。
	
	//报文头错误代码
	public final static String CHANNEL_ID_NULL_ERROR = "3001" ; //ChannelId空
	public final static String TRANSACTION_CODE_NULL_ERROR = "3002"; //TransactionCode空
	public final static String TERMINAL_ID_NULL_ERROR = "3003"; //terminalId空
	public final static String TRACE_NUMBER_NULL_ERROR = "3004"; //traceNumber空
	public final static String TRANSACTION_DATE_NULL_ERROR = "3005"; //transactionDate空
	public final static String TRANSACTION_TIME_NULL_ERROR = "3006"; //transactionTime空
	
	//hsm
	public final static String HSM_ERROR = "H0001"; //transactionTime空
}
