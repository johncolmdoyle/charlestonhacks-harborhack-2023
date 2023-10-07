import * as cdk from 'aws-cdk-lib';
import { Construct } from 'constructs';
import * as s3 from 'aws-cdk-lib/aws-s3';
import * as cloudfront from 'aws-cdk-lib/aws-cloudfront';
import * as route53 from 'aws-cdk-lib/aws-route53';
import * as targets from 'aws-cdk-lib/aws-route53-targets';
import * as aws_s3_deployment from 'aws-cdk-lib/aws-s3-deployment';
import * as acm from 'aws-cdk-lib/aws-certificatemanager';

export class LandingStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    // Create an S3 bucket for the static website content
    const websiteBucket = new s3.Bucket(this, 'WebsiteBucket');

    // Deploy the contents of the local 'website' directory to the S3 bucket
    new aws_s3_deployment.BucketDeployment(this, "Deployment", {
      sources: [aws_s3_deployment.Source.asset('./website')],
      destinationBucket: websiteBucket,
    });

    // Create a Route 53 hosted zone
    const hostedZone = route53.HostedZone.fromLookup(this, 'HostedZone', {
      domainName: 'chucktownneighbors.com',
    });

    // Create an SSL certificate
    const certificate = new acm.Certificate(this, 'WebsiteCertificate', {
      domainName: 'chucktownneighbors.com',
      subjectAlternativeNames: ['www.chucktownneighbors.com'],
      validation: acm.CertificateValidation.fromDns(hostedZone),
    });

    // Create a CloudFront distribution for the S3 bucket
    const distribution = new cloudfront.CloudFrontWebDistribution(this, 'WebsiteDistribution', {
      originConfigs: [
        {
          s3OriginSource: {
            s3BucketSource: websiteBucket,
          },
          behaviors: [{ isDefaultBehavior: true }],
        },
      ],
      viewerCertificate: cloudfront.ViewerCertificate.fromAcmCertificate(certificate, {
        aliases: ['chucktownneighbors.com', 'www.chucktownneighbors.com'],
      }),
    });

    // Create a DNS record for the CloudFront distribution in Route 53
    new route53.ARecord(this, 'AliasRecord', {
      recordName: 'chucktownneighbors.com',
      zone: hostedZone,
      target: route53.RecordTarget.fromAlias(new targets.CloudFrontTarget(distribution)),
    });

    new route53.ARecord(this, 'WwwAliasRecord', {
      recordName: 'www.chucktownneighbors.com',
      zone: hostedZone,
      target: route53.RecordTarget.fromAlias(new targets.CloudFrontTarget(distribution)),
    });
  }
}
