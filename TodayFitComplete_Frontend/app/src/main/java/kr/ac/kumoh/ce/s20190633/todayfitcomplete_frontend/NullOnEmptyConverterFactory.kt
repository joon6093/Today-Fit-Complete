package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

// Retrofit의 Converter.Factory 클래스를 상속하는 NullOnEmptyConverterFactory 클래스 정의
class NullOnEmptyConverterFactory : Converter.Factory() {

    // responseBodyConverter 메서드 재정의
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {

        // 이전의 responseBodyConverter를 가져오기 위해 Retrofit의 nextResponseBodyConverter 메서드 사용
        val nextResponseBodyConverter =
            retrofit.nextResponseBodyConverter<Any>(this, type, annotations)

        // Converter<ResponseBody, Any>을 반환
        return Converter<ResponseBody, Any> { responseBody ->

            // HTTP 응답의 내용 길이가 0이 아닌 경우
            if (responseBody.contentLength() != 0L) {
                // 이전 컨버터를 호출하여 변환하고 결과 반환
                nextResponseBodyConverter.convert(responseBody)
            } else {
                // 내용이 비어있을 경우 null 반환
                null
            }
        }
    }
}
