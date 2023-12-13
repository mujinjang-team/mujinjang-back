package mujinjang.couponsystem.common.exception;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(final ServerRequest request,
		final ErrorAttributeOptions options) {
		final Map<String, Object> map = super.getErrorAttributes(request, options);
		final Throwable throwable = getError(request);
		map.put("message", throwable.getMessage());

		if (throwable instanceof BusinessException) {
			ErrorCode errorCode = ((BusinessException)getError(request)).getErrorCode();
			map.put("errorCode", errorCode.getCode());
			map.put("status", errorCode.getStatus());
		}

		return map;
	}
}

