package easycbt2.thymeleaf_helper.dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import easycbt2.thymeleaf_helper.StringHelper;

public class StringHelperDialect implements IExpressionObjectDialect {
    // Thymeleafで使用したい名前
    private static final String STRING_HELPER_EXPRESSION_NAME = "stringhelper";

    // 名前管理するSet
    private static final Set<String> ALL_EXPRESSION_NAMES = new HashSet<String>(){
        {add(STRING_HELPER_EXPRESSION_NAME);}
    };

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {
            @Override
            public Set<String> getAllExpressionObjectNames() {
                return ALL_EXPRESSION_NAMES;
            }

            @Override
            public Object buildObject(IExpressionContext context, String expressionObjectName) {
                // 独自Utilityのインスタンスと名前を紐付け
                if(expressionObjectName.equals(STRING_HELPER_EXPRESSION_NAME)){
                    return new StringHelper();
                }
                return null;
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                // 必要に応じて実装
                return false;
            }
        };
    }

    @Override
    public String getName() {
        return "StringHelper";
    }
}