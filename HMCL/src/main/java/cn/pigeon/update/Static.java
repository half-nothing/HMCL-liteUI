package cn.pigeon.update;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

import java.util.logging.Logger;

public class Static {
    public final static OkHttpClient okHttpClient = new OkHttpClient();
    public final static String defaultMacAddress = "00-00-00-00-00-00";
    public final static String privacyAgreement =
            "在使用HMCL-liteUI.0.1.4前，需同意以下信息收集协议，否则无法正常使用我们的服务。\n" +
            "\n" +
            "在使用HMCL-liteUI.0.1.4的过程中，我们会收集您的一些隐私数据，我们十分重视你的信息的安全，这份声明将阐述我们为何、如何使用、存储你的信息。\n" +
            "除非另有规定，这份隐私声明适用于你与HMCL-liteUI.0.1.4之间的一切活动。我们可能会在不通知你的情况下变更这份声明，声明变更对所有用户都生效。\n" +
            "本声明最终解释权归Pigeon Server Team所有。\n" +
            "\n" +
            "收集的信息包括以下内容：\n" +
            "    1.游戏ID：您的游戏ID，用于在游戏中唯一标识您的帐户。\n" +
            "    2.玩家UUID：游戏中的唯一标识号，有助于我们准确识别您的帐户。\n" +
            "    3.机器Mac地址：您游戏设备的物理地址，可帮助我们识别您的设备。\n" +
            "    4.IP地址：您的网络连接IP地址，用于防止攻击和滥用、进行数据分析和调查。\n" +
            "\n" +
            "如何收集你的信息：\n" +
            "遵循最小化原则来收集信息，即只收集必要的信息。\n" +
            "在大多数情况下，大部分信息将由你自己提供。诸如IP地址等互联网通信中自动传递的信息，将直接从对应的网络请求中获取。\n" +
            "\n" +
            "如何使用你的信息：\n" +
            "在大多数情况下，我们只会使用你的信息来提供尽可能好的服务，并不断改进我们提供的服务。我们也会使用你的信息进行数据分析和调查。\n" +
            "我们不会主动在非必要的情况下将你的信息披露给第三方，也不会出于任何目的将你的信息售卖给第三方。\n" +
            "\n" +
            "用户信息的所有、保留与删除：\n" +
            "尽管我们可能会存储你的信息，但你仍然具有你的信息的所有权。在大多数情况下，我们只会在有需要时获取和存储你的信息，在使用完毕后会立即删除没有必要保留的信息。\n" +
            "可通过联系Pigeon Server Team删除你的信息，但我们也有权拒绝处理任何不合理的删除信息的请求。尽管我们可以删除我们存储的信息，但这并不代表我们能要求第三方删除其存储的信息。\n" +
            "部分信息在被删除后，可能会被重新获取或生成。";
    public final static Logger logger = Logger.getLogger("HMCL");
    public final static Gson gson = new Gson();
    public final static String separator = "\n-------------\n";
    public static int updateMaxThread = 8;
}