using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApplication1
{
    /// <summary>
    /// Сводное описание для EnableAED
    /// </summary>
    public class EnableAED : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            if(!StaticInfo.EnableAED)
                StaticInfo.EnableAED = true;
            context.Response.Write("1");
        }

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}